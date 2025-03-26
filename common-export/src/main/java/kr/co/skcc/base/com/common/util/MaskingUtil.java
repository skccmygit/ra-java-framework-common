package kr.co.skcc.base.com.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MaskingUtil {

    private static final String PHONE_NUM_PATTERN = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";
    private static final String EMAIL_PATTERN = "\\b(\\S+)@(\\S+.\\S+)";
    private static final String CARD_NUM_PATTERN = "(\\d{4})-?(\\d{4})-?(\\d{4})-?(\\d{3,4})$";

    /**
     * getMaskedId(String , String )
     *  : 마스킹 처리 함수
     *
     * @param value 마스킹처리할 데이터
     * @param gbn  마스킬 구분(email: 이메일 , phone: 휴대폰번호, card: 카드번호, name: 이름 등..)
     * @return rtn  마스킹 처리된 데이터, pattern 이 맞지 않을 경우 입력받은 값을 리턴
     *
    *
     * @author 천지은
     * @since 2021-09-28
     */
    public static String getMaskedId(String value, String gbn) {
        String rtn = value;
        switch (gbn) {
            case "name":
                rtn = getMaskedName(value);
                break;
            case "email":
                if (isEmail(value))
                    rtn = getMaskedEmail(value);
                break;
            case "phone":
                if (isPhoneNum(value))
                    rtn = getMaskedPhoneNum(value);
                break;
            case "card":
                if (isCardNum(value))
                    rtn = getMaskedCardNum(value);
                break;
            case "address":
                rtn = getMaskedAddress(value);
                break;
            case "account":
                rtn = getMaskedAccount(value);
                break;
            case "birth":
                rtn = "****-**-**";
                break;
            default:
                rtn = value;
        }
        return rtn;
    }

    /**
     * 이메일 포맷 Validator
     * @param str
     * @return isValidEmailFormat
     */
    private static boolean isEmail(final String str) {
        return isValid(EMAIL_PATTERN, str);
    }

    /**
     * 휴대폰 번호 포맷 Validator
     * @param str
     * @return isValidCellPhoneNumFormat
     */
    private static boolean isPhoneNum(final String str) {
        return isValid(PHONE_NUM_PATTERN, str);
    }

    /**
     * 카드 번호 포맷 Validator
     * @param str
     * @return isValidCardNumFormat
     */
    private static boolean isCardNum(final String str) {
        return isValid(CARD_NUM_PATTERN, str);
    }

    /**
     * 문자열이 정규식에 맞는 포맷인지 체크
     * @param regex
     * @param target
     * @return isValid
     */
    private static boolean isValid(final String regex, final String target) {
        Matcher matcher = Pattern.compile(regex).matcher(target);
        return matcher.matches();
    }

    public static String getMaskedName(String name){
        String maskedName = name; // 마스킹 이름
        String firstName = ""; // 성
        String middleName = ""; // 이름 중간
        String lastName = ""; //이름 끝
        int lastNameStartPoint; // 이름 시작 포인터

        if (!(name == null || name.isEmpty())) {
            name = name.trim();

            if (name.length() > 1) {
                firstName = name.substring(0, 1);
                lastNameStartPoint = name.indexOf(firstName);
                if (name.length() > 2) {
                    middleName = name.substring(lastNameStartPoint + 1, name.length() - 1);
                    lastName = name.substring(lastNameStartPoint + (name.length() - 1), name.length());
                } else {
                    middleName = name.substring(lastNameStartPoint + 1, name.length());
                }
                String makers = "";
                for (int i = 0; i < middleName.length(); i++) {
                    makers += "*";
                }
                lastName = middleName.replace(middleName, makers) + lastName;
                maskedName = firstName + lastName;
            }
        }
        return maskedName;

    }

    public static String getMaskedPhoneNum(String phoneNum){
        String maskedPhoneNum = phoneNum;

        Matcher matcher = Pattern.compile(PHONE_NUM_PATTERN).matcher(phoneNum);
        if (matcher.find()) {
            String target = matcher.group(2);
            char[] c = new char[target.length()];
            Arrays.fill(c, '*');
//            maskedPhoneNum = phoneNum.replace(target, String.valueOf(c));
            maskedPhoneNum = matcher.group(1) + "-" + String.valueOf(c) + "-" +  matcher.group(3);

            return maskedPhoneNum;
        }

        return maskedPhoneNum;
    }

    public static String getMaskedEmail(String email){
        /*
         * Id의 길이를 기준으로 세글자 초과인 경우 뒤 세자리를 마스킹 처리하고,
         * 세글자인 경우 뒤 두글자만 마스킹,
         * 세글자 미만인 경우 모두 마스킹 처리
         */
        String maskedEmail = email;
        Matcher matcher = Pattern.compile(EMAIL_PATTERN).matcher(email);
        if (matcher.find()) {
            String id = matcher.group(1); // 마스킹 처리할 부분인 userId

            int length = id.length();
            if (length < 3) {
                char[] c = new char[length];
                Arrays.fill(c, '*');
                maskedEmail = String.valueOf(c) + "@" + matcher.group(2);
                // maskedEmail = email.replace(id, String.valueOf(c));
            } else if (length == 3) {
                maskedEmail = email.replaceAll("\\b(\\S+)[^@][^@]+@(\\S+)", "$1**@$2");
            } else {
                length = length-2;
                char[] c = new char[length];
                Arrays.fill(c, '*');
                String asta = "";
                for (int i = 0; i < length; i++) {
                    asta = asta + "[^@]";
                }
                maskedEmail = email.replaceAll("\\b(\\S+)"+asta+"+@(\\S+)", "$1"+ String.valueOf(c) +"@$2");
            }
        }
        return maskedEmail;
    }

    public static String getMaskedCardNum(String cardNum){
        String maskedCardNum = cardNum;
        Matcher matcher = Pattern.compile(CARD_NUM_PATTERN).matcher(cardNum);
        if (matcher.find()) {
            for (int i = 3; i < 5; i++) {
                String target = matcher.group(i);
                log.debug("target ::" + target);
                int length = target.length();
                char[] c = new char[length];
                Arrays.fill(c, '*');

                maskedCardNum = maskedCardNum.replace(target, String.valueOf(c));
            }
        }
        return maskedCardNum;
    }

    public static String getMaskedAccount(String accountNum){
        String maskedAccountNum = accountNum.replaceAll("-","");

        int length = maskedAccountNum.length();
        if (length <= 8) {
            char[] c = new char[length];
            Arrays.fill(c, '*');
            maskedAccountNum = String.valueOf(c);
        } else {
            length = 8;
            char[] c = new char[length];
            Arrays.fill(c, '*');
            String asta = "";
            for (int i = 0; i < length; i++) {
                asta = asta + "[^@]";
                maskedAccountNum = accountNum.replaceAll("\\b(\\S+)" + asta, "$1"+ String.valueOf(c));
            }
        }
        return maskedAccountNum;
    }

    public static String getMaskedAddress(String address) {

        String addReg1 = "\\s*[가-힣A-Za-z\\d]+(동|로|길)";
        String addReg2 = "([가-힣a-zA-Z\\d]+(아파트|빌라|빌딩|마을))";
        String addReg3 = "([가-힣A-Za-z·\\d~\\-\\.]+(읍|번지))";
        String addReg4 = "[\\d]+(호)";
        String addReg5 = "[\\d~\\-\\.\\d]";

        Matcher matcher1 = Pattern.compile(addReg1).matcher(address);
        Matcher matcher2 = Pattern.compile(addReg2).matcher(address);
        Matcher matcher3 = Pattern.compile(addReg3).matcher(address);
        Matcher matcher4 = Pattern.compile(addReg4).matcher(address);
        Matcher matcher5 = Pattern.compile(addReg5).matcher(address);

        while (matcher1.find()) {
            String replaceAddress = null;
            if (matcher1.group().endsWith("동")) {
                replaceAddress = matcher1.group().replaceAll("[가-힣A-Za-z\\d]+(동)", "**동");
            }
            if (matcher1.group().endsWith("로")) {
                replaceAddress = matcher1.group().replaceAll("[가-힣A-Za-z\\d]+(로)", "**로");
            }
            if (matcher1.group().endsWith("길")) {
                replaceAddress = matcher1.group().replaceAll("[가-힣A-Za-z\\d]+(길)", "**길");
            }
            if (replaceAddress != null) {
                address = address.replace(matcher1.group(), replaceAddress);
            }
        }

        while (matcher2.find()){
            String replaceAddress = null;
            if (matcher2.group().contains("아파트")) {
                replaceAddress = matcher2.group().replaceAll("[가-힣a-zA-Z\\d]+(아파트)", "**아파트");
            }
            if (matcher2.group().contains("빌라")) {
                replaceAddress = matcher2.group().replaceAll("[가-힣a-zA-Z\\d]+(빌라)", "**빌라");
            }
            if (matcher2.group().contains("빌딩")) {
                replaceAddress = matcher2.group().replaceAll("[가-힣a-zA-Z\\d]+(빌딩)", "**빌딩");
            }
            if (matcher2.group().contains("마을")) {
                replaceAddress = matcher2.group().replaceAll("[가-힣a-zA-Z\\d]+(마을)", "**마을");
            }
            if (replaceAddress != null) {
                address = address.replace(matcher2.group(), replaceAddress);
            }
        }

        while (matcher3.find()) {
            String replaceAddress = null;
            if (matcher3.group().contains("읍")) {
                replaceAddress = matcher3.group().replaceAll("[가-힣A-Za-z·\\d~\\-\\.]+(읍)", "**읍");
            }
            if (matcher3.group().contains("번지")) {
                replaceAddress = matcher3.group().replaceAll("[가-힣A-Za-z·\\d~\\-\\.]+(번지)", "**번지");
            }
            if (replaceAddress != null) {
                address = address.replace(matcher3.group(), replaceAddress);
            }
        }

        while (matcher4.find()) {
            String replaceAddress;
            replaceAddress = matcher4.group().replaceAll("[\\d]+(호)","**호");
            if (replaceAddress != null) {
                address = address.replace(matcher4.group(), replaceAddress);
            }
        }

        while (matcher5.find()){
            String replaceAddress;
            replaceAddress = matcher5.group().replaceAll("[\\d]","*");
            if (replaceAddress != null) {
                address = address.replace(matcher5.group(), replaceAddress);
            }
        }
        return address;
    }

    public static String getFormatPhoneNum(String phoneNum) {
        if(phoneNum == null || "".equals(phoneNum)) return "";

        String formatPhoneNum = phoneNum;

        Matcher matcher = Pattern.compile("(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$").matcher(phoneNum);
        if (matcher.find()) {
            formatPhoneNum = matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3);
        }

        return formatPhoneNum;
    }
}
