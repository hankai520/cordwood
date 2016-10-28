package ren.hankai.cordwood.plugin.impl;

import ren.hankai.cordwood.core.domain.PluginFunction;
import ren.hankai.cordwood.plugin.ParameterMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 简单参数映射器。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 28, 2016 9:45:38 AM
 */
@Component
public class SimpleParameterMapper implements ParameterMapper {

  @Autowired
  private ConversionService conversionService;

  @Override
  public Object[] mapParameters(PluginFunction function, HttpServletRequest request,
      HttpServletResponse response) {
    List<Object> args = new ArrayList<>();
    if ((function.getParameters() != null) && (function.getParameters().length > 0)) {
      for (Parameter param : function.getParameters()) {
        if (param.getType().isAssignableFrom(HttpServletRequest.class)) {
          args.add(request);
        } else if (param.getType().isAssignableFrom(HttpServletResponse.class)) {
          args.add(response);
        } else if (param.getType().isAssignableFrom(String[].class)) {
          args.add(request.getParameterValues(param.getName()));
        } else {
          String str = request.getParameter(param.getName());
          if (conversionService.canConvert(String.class, param.getType())) {
            Object convertedParam = conversionService.convert(str, param.getType());
            args.add(convertedParam);
          }
        }
      }
    }
    return args.toArray();
  }

}
