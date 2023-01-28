package de.pierreschwang.labymod.resourcepacks.core.widget.resourcepack;

import de.pierreschwang.labymod.resourcepacks.api.dao.result.Resourcepack;
import net.labymod.api.Laby;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.resources.texture.GameImage;
import net.labymod.api.client.resources.texture.GameImageTexture;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.WebResolver;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * Represents a {@link Resourcepack} as an entry in a list of multiple resourcepacks. Based on
 * {@code StoreItemWidget} for a convenient style.
 */
@AutoWidget
@Link("resourcepack-list-widget.lss")
public class ResourcepackListEntryWidget extends SimpleWidget {

    private static final ResourceLocation FALLBACK_ICON = Laby.references().resourceLocationFactory()
            .createMinecraft("textures/misc/unknown_pack.png");

    private final Resourcepack resourcepack;
    private Supplier<ResourceLocation> iconResourceLocation;

    public ResourcepackListEntryWidget(@NotNull Resourcepack resourcepack) {
        this.resourcepack = resourcepack;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        this.addChild(buildResoucepackItem());
    }

    private Widget buildResoucepackItem() {
        ResourceLocation iconLocation = resourceLocationForIcon();
        if (Laby.references().textureRepository().hasResource(iconLocation)) {
            this.iconResourceLocation = () -> iconLocation;
        } else {
            this.iconResourceLocation = () -> FALLBACK_ICON;
            // Attempt to download the icon of the resourcepack
            String url = resourcepack.thumbnail();
            // dumb behaviour by RP24
            if (url.equals("https://resourcepacks24.de/storage/img/demo_pack.webp")) {
                url = "https://resourcepacks24.de/storage/img/demo_pack.png";
            }
            WebResolver.resolve(Request.ofInputStream().async().url(url), webInputStreamResponse -> {
                // Linked thumbnail does not exist somehow
                if (!webInputStreamResponse.isPresent()) {
                    return;
                }
                try {
                    GameImageTexture imageTexture = Laby.references().gameImageTextureFactory()
                            .create(iconLocation, GameImage.IMAGE_PROVIDER.getImage(webInputStreamResponse.get().getInputStream()));
                    Laby.references().textureRepository().registerTexture(iconLocation, imageTexture, () -> {
                        this.iconResourceLocation = () -> iconLocation;
                        reInitialize();
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        FlexibleContentWidget wrapper = new FlexibleContentWidget().addId("resourcepack-wrapper");
        IconWidget thumbnail = new IconWidget(Icon.texture(iconResourceLocation))
                .addId("resourcepack-thumbnail");
        wrapper.addContent(thumbnail);
        DivWidget textWrapper = new DivWidget().addId("resourcepack-text-wrapper");
        // TODO: add rating
        HorizontalListWidget nameWrapper = new HorizontalListWidget().addId(
                "resourcepack-name-wrapper");
        nameWrapper.addEntry(
                ComponentWidget.text(resourcepack.name()).addId("resourcepack-name"));
        nameWrapper.addEntry(ComponentWidget.text(resourcepack.username(), NamedTextColor.GREEN)
                .addId("resourcepack-author"));
        textWrapper.addChild(nameWrapper);

        ComponentWidget descriptionWrapper = ComponentWidget.text(resourcepack.description())
                .addId("resourcepack-description-wrapper");
        textWrapper.addChild(descriptionWrapper);

        wrapper.addFlexibleContent(textWrapper);
        // TODO: button
        return wrapper;
    }

    private ResourceLocation resourceLocationForIcon() {
        return Laby.references().resourceLocationFactory()
                .create("resourcepacks24", "icon/" + resourcepack.id());
    }

}