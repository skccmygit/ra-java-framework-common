package kr.co.skcc.oss.com.common.aspect;

import kr.co.skcc.oss.com.common.annotation.ApplyMasking;
import kr.co.skcc.oss.com.common.annotation.MaskRequired;
import kr.co.skcc.oss.com.common.util.MaskingUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Aspect
@EnableAspectJAutoProxy
@Component
@Slf4j
public class MaskingAspect {

    @Around("@annotation(applyMasking)")
    public Object applyMaskingAspect(ProceedingJoinPoint joinPoint, ApplyMasking applyMasking) throws Throwable {
        Object response = joinPoint.proceed();
        if(response != null){
            return applyMaskingUtil(applyMasking.typeValue(), applyMasking.genericTypeValue(), response);
        }else {
            return null;
        }
    }

    private static <T> T applyMaskingUtil(Class<?> clazz, Class<?> klass, Object response) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(response instanceof Page) {
            return applyMaskingUtilForPage(klass, response);
        } else if(response instanceof List){
            return applyMaskingUtilForList(klass, response);
        } else{
            return applyMaskingUtilForDto(clazz, response);
        }
    }

    private static <T> T applyMaskingUtilForDto(Class<?> clazz, Object response) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        Object responseDto = clazz.getDeclaredConstructor().newInstance();
        Arrays.stream(fields).forEach(
                field -> {
                    field.setAccessible(true);
                    try{
                        Object fieldValue = field.get(response);
                        if(fieldValue instanceof String && field.isAnnotationPresent(MaskRequired.class)){
                            MaskRequired maskRequired = field.getAnnotation(MaskRequired.class);
                            String maskingType  = maskRequired.type();
                            String maskedValue = MaskingUtil.getMaskedId((String)fieldValue, maskingType);
                            field.set(responseDto, maskedValue);
                        }else{
                            field.set(responseDto, fieldValue);
                        }
                    } catch (Exception e) {}
                }
        );
        return (T) responseDto;
    }

    private static <T> T applyMaskingUtilForList(Class<?> klass, Object response) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Object> responseDtoList = new ArrayList<>();
        List<?> responseList = (List<?>) response;
        for(Object responseDto : responseList){
            if(responseDto != null && responseDto.getClass().equals(klass)){
                Object maskedResponseDto = applyMaskingUtilForDto(klass, responseDto);
                responseDtoList.add(maskedResponseDto);
            }else{
                responseDtoList.add(responseDto);
            }
        }
        return (T) responseDtoList;
    }

    private static <T> T applyMaskingUtilForPage(Class<?> klass, Object response) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        Page<T> pageObj = (Page<T>) response;
        List<?> responseList = pageObj.getContent();
        List<Object> responseDtoList = pageObj.getTotalElements() > 0 ? applyMaskingUtilForList(klass, responseList) : new ArrayList<>();

        return (T) new PageImpl<>(responseDtoList, pageObj.getPageable(), pageObj.getTotalElements());
    }

}
