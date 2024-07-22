package dev.xkmc.l2tabs.init.data;

import dev.xkmc.l2core.util.ConfigInit;
import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class L2TabsConfig {

	public static class Client extends ConfigInit {

		public final ModConfigSpec.BooleanValue showTabs;
		public final ModConfigSpec.BooleanValue showTabsOnlyCurio;
		public final ModConfigSpec.BooleanValue redirectInventoryTabToCuriosInventory;
		public final ModConfigSpec.IntValue attributeLinePerPage;
		public final ModConfigSpec.EnumValue<AttrDispEntry.AttributeDisplay> attributeSettings;

		public final ModConfigSpec.ConfigValue<List<? extends String>> hiddenTabs;

		Client(Builder builder) {
			markL2();
			showTabs = builder.text("Show inventory tabs")
					.define("showTabs", true);
			showTabsOnlyCurio = builder.text("Tabs with curios only").comment(
					"Show inventory tabs only in curio page.",
					"Only works when showTabs is true and curio is installed.",
					"For users who have other tabs conflicting with this mod"
			).define("showTabsOnlyCurio", false);
			redirectInventoryTabToCuriosInventory = builder.text("Redirect Inventory Tab to Curios Inventory")
					.define("redirectInventoryTabToCuriosInventory", true);
			attributeLinePerPage = builder.text("Number of attribute lines per page")
					.defineInRange("attributeLinePerPage", 15, 1, 100);
			attributeSettings = builder.text("Attribute display")
					.comment("COMMON: Show only common attributes and L2 attributes")
					.comment("ALL: Show all attributes in attribute type, similar to Apothic Attributes")
					.comment("ALL_EXCEPT_UNCHANGED: Show all, but hide attributes that are unchanged")
					.defineEnum("attributeSettings", AttrDispEntry.AttributeDisplay.COMMON);

			Lazy<Set<String>> keys = Lazy.of(() -> L2Tabs.TABS.get().keySet().stream()
					.map(ResourceLocation::getPath)
					.collect(Collectors.toSet()));

			hiddenTabs = builder.text("Hidden Tabs").comment("List of tabs to hide")
					.comment("Example: \"attribute\" for attribute tab")
					.comment("Example: \"curios\" for curios tab")
					.comment("Example: \"pandora\" for pandora tab")
					.defineListAllowEmpty("hiddenTabs", new ArrayList<>(List.of()),
							() -> "attribute", e -> keys.get().contains((String) e));
		}

	}

	public static final Client CLIENT = L2Tabs.REGISTRATE.registerClient(Client::new);

	public static void init() {
	}

}
