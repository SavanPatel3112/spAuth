package com.example.authmoduls.common.decorator;

import com.example.authmoduls.auth.decorator.bookDecorator.BookDataResponseExcel;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

public class NullAwareBeanUtilsBean extends BeanUtilsBean {

        @Override
        public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
            if(value==null)return;
            super.copyProperty(dest, name, value);
        }

}
