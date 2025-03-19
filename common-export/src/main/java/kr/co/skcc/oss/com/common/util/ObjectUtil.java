package kr.co.skcc.oss.com.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ObjectUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void copyNonNullProperties(Object source, Object target) throws BeansException {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t -> {
            final List<?> keys = Arrays.stream(keyExtractors).map(ke -> ke.apply(t)).collect(Collectors.toList());
            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }
    public static <T> T toDto(Map<String, Object> map, Class<T> dto){
        try {
            return map == null || map.isEmpty() ? dto.getDeclaredConstructor().newInstance() : OBJECT_MAPPER.convertValue(map, dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toDtoList(List<Map> list, Class<T> dto){
        return list == null ? Collections.emptyList() : (List<T>) list.stream().map(map -> toDto(map, dto)).collect(Collectors.toList());
    }

    public static <T> Page<T> toDtoPage(Page<Map> pList, Class<T> dto){

        List<T> responseDtoList = pList.getTotalElements() > 0 ? toDtoList(pList.getContent(), dto) : new ArrayList<>();
        return new PageImpl<T>(responseDtoList, pList.getPageable(), pList.getTotalElements());
    }
}
