package ren.hankai.cordwood.plugin.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import ren.hankai.cordwood.plugin.FunctionParameter;
import ren.hankai.cordwood.plugin.PluginFunction;
import ren.hankai.cordwood.plugin.api.ParameterMapper;

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
    final List<Object> args = new ArrayList<>();
    if ((function.getParameters() != null) && (function.getParameters().length > 0)) {
      for (final FunctionParameter param : function.getParameters()) {
        if (param.getType().isAssignableFrom(HttpServletRequest.class)) {
          args.add(request);
        } else if (param.getType().isAssignableFrom(HttpServletResponse.class)) {
          args.add(response);
        } else if (param.getType().isAssignableFrom(String[].class)) {
          args.add(request.getParameterValues(param.getName()));
        } else {
          final String str = request.getParameter(param.getName());
          if (str == null) {
            args.add(null);
          } else {
            if (conversionService.canConvert(String.class, param.getType())) {
              final Object convertedParam = conversionService.convert(str, param.getType());
              args.add(convertedParam);
            }
          }
        }
      }
    }
    return args.toArray();
  }

}
