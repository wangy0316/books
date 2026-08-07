package com.example.demo.common;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class PageArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(@NonNull MethodParameter parameter) {
    // 1. 参数类型必须是 Page
    // 2. 参数上必须有 @AutoPage 注解
    return parameter.getParameterType().equals(Page.class)
        && parameter.hasParameterAnnotation(AutoPage.class);
  }

  @Override
  public Object resolveArgument(@NonNull MethodParameter parameter,
      @NonNull ModelAndViewContainer mavContainer,
      @NonNull NativeWebRequest webRequest,
      @NonNull WebDataBinderFactory binderFactory) {

    AutoPage autoPage = parameter.getParameterAnnotation(AutoPage.class);

    // 从请求参数中获取 pageNum 和 pageSize，若不存在则使用默认值
    String pageNumStr = webRequest.getParameter("pageNum");
    String pageSizeStr = webRequest.getParameter("pageSize");

    int pageNum = (pageNumStr != null) ? Integer.parseInt(pageNumStr) : autoPage.defaultPage();
    int pageSize = (pageSizeStr != null) ? Integer.parseInt(pageSizeStr) : autoPage.defaultSize();

    // 简单校验，防止负数
    if (pageNum < 1)
      pageNum = 1;
    if (pageSize < 1)
      pageSize = 10;
    if (pageSize > 100)
      pageSize = 100; // 防止过大查询

    // 创建 Page 对象
    // 注意：这里不需要指定泛型 T，因为 MP 的 Page 在运行时通过反射或后续操作确定类型
    // 但为了类型安全，通常我们在 Controller 中声明 Page<User>，这里返回 raw type 或 Page<?>
    return new Page<>(pageNum, pageSize);
  }
}
