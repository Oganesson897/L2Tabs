package dev.xkmc.l2tabs.init.data;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class L2TabsConfig {

	public static class Client {

		public final ModConfigSpec.BooleanValue showTabs;
		public final ModConfigSpec.BooleanValue showTabsOnlyCurio;
		public final ModConfigSpec.BooleanValue redirectInventoryTabToCuriosInventory;
		public final ModConfigSpec.IntValue attributeLinePerPage;

		public final ModConfigSpec.ConfigValue<List<String>> hiddenTabs;

		Client(ModConfigSpec.Builder builder) {
			showTabs = builder.comment("Show inventory tabs")
					.define("showTabs", true);
			showTabsOnlyCurio = builder.comment("Show inventory tabs only in curio page. Only works when showTabs is true and curio is installed.")
					.define("showTabsOnlyCurio", false);
			redirectInventoryTabToCuriosInventory = builder.comment("Redirect Inventory Tab to Curios Inventory")
					.define("redirectInventoryTabToCuriosInventory", true);
			attributeLinePerPage = builder.comment("Number of attribure lines per page")
					.defineInRange("attributeLinePerPage", 15, 1, 100);

			hiddenTabs = builder.comment("List of tabs to hide. Use title translation key for tab id.")
					.comment("Example: menu.tabs.attribute for attribute tab")
					.comment("Example: menu.tabs.curios for curios tab")
					.comment("Example: pandora.menu.title for pandora tab")
					.define("hiddentTabs", new ArrayList<>(List.of()));
		}

	}

	public static class Server {

		public final ModConfigSpec.BooleanValue syncPlayerAttributeName;
		public final ModConfigSpec.BooleanValue syncAllEntityAttributeName;

		Server(ModConfigSpec.Builder builder) {
			syncPlayerAttributeName = builder.comment("Sync player attribute names to client")
					.define("syncPlayerAttributeName", true);
			syncAllEntityAttributeName = builder.comment("Sync all entity attribute name to client")
					.define("syncAllEntityAttributeName", false);
		}

	}

	public static final ModConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	public static final ModConfigSpec SERVER_SPEC;
	public static final Server SERVER;

	static {
		final Pair<Client, ModConfigSpec> client = new ModConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();

		final Pair<Server, ModConfigSpec> common = new ModConfigSpec.Builder().configure(Server::new);
		SERVER_SPEC = common.getRight();
		SERVER = common.getLeft();
	}

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
		register(ModConfig.Type.CLIENT, CLIENT_SPEC);
		register(ModConfig.Type.SERVER, SERVER_SPEC);
	}

	private static void register(ModConfig.Type type, IConfigSpec<?> spec) {
		var mod = ModLoadingContext.get().getActiveContainer();
		String path = "l2_configs/" + mod.getModId() + "-" + type.extension() + ".toml";
		ModLoadingContext.get().registerConfig(type, spec, path);
	}


}
