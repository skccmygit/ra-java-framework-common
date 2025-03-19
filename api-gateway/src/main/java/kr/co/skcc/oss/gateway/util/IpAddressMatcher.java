package kr.co.skcc.oss.gateway.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * ArgosIpAddressMatcher.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-05-18, 최초 작성
 */
@Component
public class IpAddressMatcher {

    private List<String> whitelistIp;

    private int[] nMaskBits;

    private InetAddress[] requiredAddress;


    @Autowired
    public IpAddressMatcher(@Value("${app.ip.whitelist}") List<String> yamlWhiteList ){

        this.whitelistIp = yamlWhiteList;
        if(whitelistIp != null){

            nMaskBits = new int[whitelistIp.size()];
            requiredAddress = new InetAddress[whitelistIp.size()];

            for ( int idx = 0 ; idx < whitelistIp.size() ; idx++ ){

                String ipAddress = whitelistIp.get(idx);
                if (ipAddress.indexOf('/') > 0) {
                    String[] addressAndMask = StringUtils.split(ipAddress, "/");
                    ipAddress = addressAndMask[0];
                    this.nMaskBits[idx] = Integer.parseInt(addressAndMask[1]);
                }
                else {
                    this.nMaskBits[idx] = -1;
                }
                this.requiredAddress[idx] = parseAddress(ipAddress);
            }
        }
    }

    private boolean listMatches(InetAddress remoteAddress, int iter){

        if (!this.requiredAddress[iter].getClass().equals(remoteAddress.getClass())) {
            return false;
        }

        if (this.nMaskBits[iter] < 0) {
            return remoteAddress.equals(this.requiredAddress[iter]);
        }

        byte[] remAddr = remoteAddress.getAddress();
        byte[] reqAddr = this.requiredAddress[iter].getAddress();
        int nMaskFullBytes = this.nMaskBits[iter] / 8;
        byte finalByte = (byte) (0xFF00 >> (this.nMaskBits[iter] & 0x07));

        for (int i = 0; i < nMaskFullBytes; i++) {
            if (remAddr[i] != reqAddr[i]) {
                return false;
            }
        }
        if (finalByte != 0) {
            return (remAddr[nMaskFullBytes] & finalByte) == (reqAddr[nMaskFullBytes] & finalByte);
        }
        return true;
    }

    public boolean matches(String address) {
        InetAddress remoteAddress = parseAddress(address);

        boolean matchFlag = false;

        for ( int iter = 0 ; iter < this.requiredAddress.length ; iter++){

            matchFlag = listMatches(remoteAddress, iter);

            if(matchFlag){
                return matchFlag;
            }
        }
        return matchFlag;
    }

    private InetAddress parseAddress(String address) {
        try {
            return InetAddress.getByName(address);
        }
        catch (UnknownHostException ex) {
            throw new IllegalArgumentException("Failed to parse address" + address, ex);
        }
    }

    public List<String> getWhitelistIp() {
        return this.whitelistIp;
    }
}
