package org.jtwig.spring.asset;

import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.extension.Extension;
import org.jtwig.spring.asset.function.JtwigAssetFunction;

public class SpringAssetExtension implements Extension {
    @Override
    public void configure(EnvironmentConfigurationBuilder configurationBuilder) {
        configurationBuilder
                .functions()
                    .add(new JtwigAssetFunction())
                .and();
    }
}
