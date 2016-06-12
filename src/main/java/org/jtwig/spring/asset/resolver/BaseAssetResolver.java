package org.jtwig.spring.asset.resolver;

public class BaseAssetResolver implements AssetResolver {
    private String prefix = "/";

    @Override
    public String resolve(String asset) {
        String relativePathToAsset = asset;
        if (relativePathToAsset.startsWith("/"))
            relativePathToAsset = relativePathToAsset.substring(1);
        return String.format("%s/%s", prefix, relativePathToAsset);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
