package com.yukikase.lib.testclasses;

import com.destroystokyo.paper.entity.ai.MobGoals;
import io.papermc.paper.ban.BanListType;
import io.papermc.paper.configuration.ServerConfiguration;
import io.papermc.paper.datapack.DatapackManager;
import io.papermc.paper.math.Position;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.*;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityFactory;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.packs.DataPackManager;
import org.bukkit.packs.ResourcePack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;

public class ServerTest implements Server {

    private final CommandMap commandMap = mock(SimpleCommandMap.class);

    public CommandMap getCommandMap() {
        return commandMap;
    }

    @Override
    public @NotNull File getPluginsFolder() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getVersion() {
        return "";
    }

    @Override
    public String getBukkitVersion() {
        return "";
    }

    @Override
    public @NotNull String getMinecraftVersion() {
        return "";
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return List.of();
    }

    @Override
    public int getMaxPlayers() {
        return 0;
    }

    @Override
    public void setMaxPlayers(int i) {

    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public int getViewDistance() {
        return 0;
    }

    @Override
    public int getSimulationDistance() {
        return 0;
    }

    @Override
    public String getIp() {
        return "";
    }

    @Override
    public String getWorldType() {
        return "";
    }

    @Override
    public boolean getGenerateStructures() {
        return false;
    }

    @Override
    public int getMaxWorldSize() {
        return 0;
    }

    @Override
    public boolean getAllowEnd() {
        return false;
    }

    @Override
    public boolean getAllowNether() {
        return false;
    }

    @Override
    public boolean isLoggingIPs() {
        return false;
    }

    @Override
    public List<String> getInitialEnabledPacks() {
        return List.of();
    }

    @Override
    public List<String> getInitialDisabledPacks() {
        return List.of();
    }

    @Override
    public DataPackManager getDataPackManager() {
        return null;
    }

    @Override
    public ServerTickManager getServerTickManager() {
        return null;
    }

    @Override
    public ResourcePack getServerResourcePack() {
        return null;
    }

    @Override
    public String getResourcePack() {
        return "";
    }

    @Override
    public String getResourcePackHash() {
        return "";
    }

    @Override
    public String getResourcePackPrompt() {
        return "";
    }

    @Override
    public boolean isResourcePackRequired() {
        return false;
    }

    @Override
    public boolean hasWhitelist() {
        return false;
    }

    @Override
    public void setWhitelist(boolean b) {

    }

    @Override
    public boolean isWhitelistEnforced() {
        return false;
    }

    @Override
    public void setWhitelistEnforced(boolean b) {

    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        return Set.of();
    }

    @Override
    public void reloadWhitelist() {

    }

    @Override
    public int broadcastMessage(String s) {
        return 0;
    }

    @Override
    public String getUpdateFolder() {
        return "";
    }

    @Override
    public File getUpdateFolderFile() {
        return null;
    }

    @Override
    public long getConnectionThrottle() {
        return 0;
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        return 0;
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        return 0;
    }

    @Override
    public int getTicksPerWaterSpawns() {
        return 0;
    }

    @Override
    public int getTicksPerWaterAmbientSpawns() {
        return 0;
    }

    @Override
    public int getTicksPerWaterUndergroundCreatureSpawns() {
        return 0;
    }

    @Override
    public int getTicksPerAmbientSpawns() {
        return 0;
    }

    @Override
    public int getTicksPerSpawns(SpawnCategory spawnCategory) {
        return 0;
    }

    @Override
    public Player getPlayer(String s) {
        return null;
    }

    @Override
    public Player getPlayerExact(String s) {
        return null;
    }

    @Override
    public List<Player> matchPlayer(String s) {
        return List.of();
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public @Nullable UUID getPlayerUniqueId(@NotNull String s) {
        return null;
    }

    @Override
    public PluginManager getPluginManager() {
        return null;
    }

    @Override
    public BukkitScheduler getScheduler() {
        return null;
    }

    @Override
    public ServicesManager getServicesManager() {
        return null;
    }

    @Override
    public List<World> getWorlds() {
        return List.of();
    }

    @Override
    public boolean isTickingWorlds() {
        return false;
    }

    @Override
    public World createWorld(WorldCreator worldCreator) {
        return null;
    }

    @Override
    public boolean unloadWorld(String s, boolean b) {
        return false;
    }

    @Override
    public boolean unloadWorld(World world, boolean b) {
        return false;
    }

    @Override
    public World getWorld(String s) {
        return null;
    }

    @Override
    public World getWorld(UUID uuid) {
        return null;
    }

    @Override
    public @Nullable World getWorld(@NotNull Key key) {
        return null;
    }

    @Override
    public WorldBorder createWorldBorder() {
        return null;
    }

    @Override
    public MapView getMap(int i) {
        return null;
    }

    @Override
    public MapView createMap(World world) {
        return null;
    }

    @Override
    public ItemStack createExplorerMap(World world, Location location, StructureType structureType) {
        return null;
    }

    @Override
    public ItemStack createExplorerMap(World world, Location location, StructureType structureType, int i, boolean b) {
        return null;
    }

    @Override
    public @Nullable ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, org.bukkit.generator.structure.@NotNull StructureType structureType, MapCursor.@NotNull Type type, int i, boolean b) {
        return null;
    }

    @Override
    public void reload() {

    }

    @Override
    public void reloadData() {

    }

    @Override
    public void updateResources() {

    }

    @Override
    public void updateRecipes() {

    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public PluginCommand getPluginCommand(String s) {
        return null;
    }

    @Override
    public void savePlayers() {

    }

    @Override
    public boolean dispatchCommand(CommandSender commandSender, String s) throws CommandException {
        return false;
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        return false;
    }

    @Override
    public boolean addRecipe(@Nullable Recipe recipe, boolean b) {
        return false;
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack itemStack) {
        return List.of();
    }

    @Override
    public Recipe getRecipe(NamespacedKey namespacedKey) {
        return null;
    }

    @Override
    public Recipe getCraftingRecipe(ItemStack[] itemStacks, World world) {
        return null;
    }

    @Override
    public ItemStack craftItem(ItemStack[] itemStacks, World world, Player player) {
        return null;
    }

    @Override
    public ItemStack craftItem(ItemStack[] itemStacks, World world) {
        return null;
    }

    @Override
    public ItemCraftResult craftItemResult(ItemStack[] itemStacks, World world, Player player) {
        return null;
    }

    @Override
    public ItemCraftResult craftItemResult(ItemStack[] itemStacks, World world) {
        return null;
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        return null;
    }

    @Override
    public void clearRecipes() {

    }

    @Override
    public void resetRecipes() {

    }

    @Override
    public boolean removeRecipe(NamespacedKey namespacedKey) {
        return false;
    }

    @Override
    public boolean removeRecipe(@NotNull NamespacedKey namespacedKey, boolean b) {
        return false;
    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        return Map.of();
    }

    @Override
    public int getSpawnRadius() {
        return 0;
    }

    @Override
    public void setSpawnRadius(int i) {

    }

    @Override
    public boolean shouldSendChatPreviews() {
        return false;
    }

    @Override
    public boolean isEnforcingSecureProfiles() {
        return false;
    }

    @Override
    public boolean isAcceptingTransfers() {
        return false;
    }

    @Override
    public boolean getHideOnlinePlayers() {
        return false;
    }

    @Override
    public boolean getOnlineMode() {
        return false;
    }

    @Override
    public @NotNull ServerConfiguration getServerConfig() {
        return null;
    }

    @Override
    public boolean getAllowFlight() {
        return false;
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public int broadcast(String s, String s1) {
        return 0;
    }

    @Override
    public int broadcast(@NotNull Component component, @NotNull String s) {
        return 0;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String s) {
        return null;
    }

    @Override
    public @Nullable OfflinePlayer getOfflinePlayerIfCached(@NotNull String s) {
        return null;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return null;
    }

    @Override
    public PlayerProfile createPlayerProfile(UUID uuid, String s) {
        return null;
    }

    @Override
    public PlayerProfile createPlayerProfile(UUID uuid) {
        return null;
    }

    @Override
    public PlayerProfile createPlayerProfile(String s) {
        return null;
    }

    @Override
    public Set<String> getIPBans() {
        return Set.of();
    }

    @Override
    public void banIP(String s) {

    }

    @Override
    public void unbanIP(String s) {

    }

    @Override
    public void banIP(InetAddress inetAddress) {

    }

    @Override
    public void unbanIP(InetAddress inetAddress) {

    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        return Set.of();
    }

    @Override
    public <T extends BanList<?>> T getBanList(BanList.Type type) {
        return null;
    }

    @Override
    public <B extends BanList<E>, E> @NotNull B getBanList(@NotNull BanListType<B> banListType) {
        return null;
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        return Set.of();
    }

    @Override
    public GameMode getDefaultGameMode() {
        return null;
    }

    @Override
    public void setDefaultGameMode(GameMode gameMode) {

    }

    @Override
    public boolean forcesDefaultGameMode() {
        return false;
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return null;
    }

    @Override
    public @NotNull CommandSender createCommandSender(@NotNull Consumer<? super Component> consumer) {
        return null;
    }

    @Override
    public File getWorldContainer() {
        return null;
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        return new OfflinePlayer[0];
    }

    @Override
    public Messenger getMessenger() {
        return null;
    }

    @Override
    public HelpMap getHelpMap() {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, InventoryType inventoryType) {
        return null;
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder inventoryHolder, @NotNull InventoryType inventoryType, @NotNull Component component) {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, InventoryType inventoryType, String s) {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, int i) throws IllegalArgumentException {
        return null;
    }

    @Override
    public @NotNull Inventory createInventory(@Nullable InventoryHolder inventoryHolder, int i, @NotNull Component component) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Inventory createInventory(InventoryHolder inventoryHolder, int i, String s) throws IllegalArgumentException {
        return null;
    }

    @Override
    public @NotNull Merchant createMerchant(@Nullable Component component) {
        return null;
    }

    @Override
    public Merchant createMerchant(String s) {
        return null;
    }

    @Override
    public Merchant createMerchant() {
        return null;
    }

    @Override
    public int getMaxChainedNeighborUpdates() {
        return 0;
    }

    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public int getWaterUndergroundCreatureSpawnLimit() {
        return 0;
    }

    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public int getSpawnLimit(SpawnCategory spawnCategory) {
        return 0;
    }

    @Override
    public boolean isPrimaryThread() {
        return false;
    }

    @Override
    public @NotNull Component motd() {
        return null;
    }

    @Override
    public void motd(@NotNull Component component) {

    }

    @Override
    public @Nullable Component shutdownMessage() {
        return null;
    }

    @Override
    public String getMotd() {
        return "";
    }

    @Override
    public void setMotd(String s) {

    }

    @Override
    public ServerLinks getServerLinks() {
        return null;
    }

    @Override
    public String getShutdownMessage() {
        return "";
    }

    @Override
    public Warning.WarningState getWarningState() {
        return null;
    }

    @Override
    public ItemFactory getItemFactory() {
        return null;
    }

    @Override
    public EntityFactory getEntityFactory() {
        return null;
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return null;
    }

    @Override
    public Criteria getScoreboardCriteria(String s) {
        return null;
    }

    @Override
    public CachedServerIcon getServerIcon() {
        return null;
    }

    @Override
    public CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
        return null;
    }

    @Override
    public CachedServerIcon loadServerIcon(BufferedImage bufferedImage) throws IllegalArgumentException, Exception {
        return null;
    }

    @Override
    public void setIdleTimeout(int i) {

    }

    @Override
    public int getIdleTimeout() {
        return 0;
    }

    @Override
    public int getPauseWhenEmptyTime() {
        return 0;
    }

    @Override
    public void setPauseWhenEmptyTime(int i) {

    }

    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        return null;
    }

    @Override
    public BossBar createBossBar(String s, BarColor barColor, BarStyle barStyle, BarFlag... barFlags) {
        return null;
    }

    @Override
    public KeyedBossBar createBossBar(NamespacedKey namespacedKey, String s, BarColor barColor, BarStyle barStyle, BarFlag... barFlags) {
        return null;
    }

    @Override
    public Iterator<KeyedBossBar> getBossBars() {
        return null;
    }

    @Override
    public KeyedBossBar getBossBar(NamespacedKey namespacedKey) {
        return null;
    }

    @Override
    public boolean removeBossBar(NamespacedKey namespacedKey) {
        return false;
    }

    @Override
    public Entity getEntity(UUID uuid) {
        return null;
    }

    @Override
    public double @NotNull [] getTPS() {
        return new double[0];
    }

    @Override
    public long @NotNull [] getTickTimes() {
        return new long[0];
    }

    @Override
    public double getAverageTickTime() {
        return 0;
    }

    @Override
    public Advancement getAdvancement(NamespacedKey namespacedKey) {
        return null;
    }

    @Override
    public Iterator<Advancement> advancementIterator() {
        return null;
    }

    @Override
    public BlockData createBlockData(Material material) {
        return null;
    }

    @Override
    public BlockData createBlockData(Material material, Consumer<? super BlockData> consumer) {
        return null;
    }

    @Override
    public BlockData createBlockData(String s) throws IllegalArgumentException {
        return null;
    }

    @Override
    public BlockData createBlockData(Material material, String s) throws IllegalArgumentException {
        return null;
    }

    @Override
    public <T extends Keyed> Tag<T> getTag(String s, NamespacedKey namespacedKey, Class<T> aClass) {
        return null;
    }

    @Override
    public <T extends Keyed> Iterable<Tag<T>> getTags(String s, Class<T> aClass) {
        return null;
    }

    @Override
    public LootTable getLootTable(NamespacedKey namespacedKey) {
        return null;
    }

    @Override
    public List<Entity> selectEntities(CommandSender commandSender, String s) throws IllegalArgumentException {
        return List.of();
    }

    @Override
    public StructureManager getStructureManager() {
        return null;
    }

    @Override
    public <T extends Keyed> Registry<T> getRegistry(Class<T> aClass) {
        return null;
    }

    @Override
    public UnsafeValues getUnsafe() {
        return null;
    }

    @Override
    public Spigot spigot() {
        return null;
    }

    @Override
    public void restart() {

    }

    @Override
    public void reloadPermissions() {

    }

    @Override
    public boolean reloadCommandAliases() {
        return false;
    }

    @Override
    public boolean suggestPlayerNamesWhenNullTabCompletions() {
        return false;
    }

    @Override
    public @NotNull String getPermissionMessage() {
        return "";
    }

    @Override
    public @NotNull Component permissionMessage() {
        return null;
    }

    @Override
    public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfile(@NotNull UUID uuid) {
        return null;
    }

    @Override
    public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfile(@NotNull String s) {
        return null;
    }

    @Override
    public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfile(@Nullable UUID uuid, @Nullable String s) {
        return null;
    }

    @Override
    public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfileExact(@Nullable UUID uuid, @Nullable String s) {
        return null;
    }

    @Override
    public int getCurrentTick() {
        return 0;
    }

    @Override
    public boolean isStopping() {
        return false;
    }

    @Override
    public @NotNull MobGoals getMobGoals() {
        return null;
    }

    @Override
    public @NotNull DatapackManager getDatapackManager() {
        return null;
    }

    @Override
    public @NotNull PotionBrewer getPotionBrewer() {
        return null;
    }

    @Override
    public @NotNull RegionScheduler getRegionScheduler() {
        return null;
    }

    @Override
    public @NotNull AsyncScheduler getAsyncScheduler() {
        return null;
    }

    @Override
    public @NotNull GlobalRegionScheduler getGlobalRegionScheduler() {
        return null;
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, @NotNull Position position) {
        return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, @NotNull Position position, int i) {
        return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Location location) {
        return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Location location, int i) {
        return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, int i, int i1) {
        return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull World world, int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean isOwnedByCurrentRegion(@NotNull Entity entity) {
        return false;
    }

    @Override
    public boolean isGlobalTickThread() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public void allowPausing(@NotNull Plugin plugin, boolean b) {

    }

    @Override
    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return Set.of();
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return null;
    }
}
