package org.jtwig.spring.asset.function;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.SimpleJtwigFunction;
import org.jtwig.spring.asset.resolver.AssetResolver;
import org.jtwig.web.servlet.ServletRequestHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class JtwigAssetFunction extends SimpleJtwigFunction {
    @Override
    public String name() {
        return "asset";
    }

    @Override
    public Object execute(FunctionRequest request) {
        request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
        String path = request.get(0).toString();

        return getAssetResolver().resolve(path);
    }

    protected AssetResolver getAssetResolver () {
        return WebApplicationContextUtils.getWebApplicationContext(ServletRequestHolder.get().getServletContext())
                .getBean(AssetResolver.class);
    }
}
