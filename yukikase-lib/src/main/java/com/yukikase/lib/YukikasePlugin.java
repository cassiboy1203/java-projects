package com.yukikase.lib;

import com.comphenix.protocol.ProtocolLibrary;
import com.yukikase.framework.injection.Injector;
import com.yukikase.lib.packet.event.PacketEvent;
import com.yukikase.lib.task.Task;
import com.yukikase.lib.task.Timer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public abstract class YukikasePlugin extends JavaPlugin {
    protected Injector injector;
    private final Map<Timer, BukkitTask> runningTimers = new HashMap<>();
    private final Map<Task, BukkitTask> runningTasks = new HashMap<>();

    public final void useClassLoader() {
        Thread.currentThread().setContextClassLoader(getClassLoader());
    }

    public Class<?> permissionRegister() {
        return null;
    }

    @Override
    public final void onDisable() {
        doDisable();
        for (var timer : runningTimers.values()) {
            timer.cancel();
        }
        for (var task : runningTasks.entrySet()) {
            task.getKey().onCancel();
            task.getValue().cancel();
        }
    }

    @Override
    public final void onEnable() {
        injector = register();

        doEnable();
    }

    protected void doEnable() {
    }

    protected void doDisable() {
    }

    @SuppressWarnings("unchecked")
    protected <T extends JavaPlugin> T getRequiredDependency(String name) {
        T dependency = (T) Bukkit.getPluginManager().getPlugin(name);

        if (dependency == null) {
            getLogger().log(Level.SEVERE, "Missing dependency: " + name);
            Bukkit.getPluginManager().disablePlugin(this);
            throw new IllegalStateException("Missing dependency: " + name);
        }

        return dependency;
    }

    @SuppressWarnings("unchecked")
    protected <T extends JavaPlugin> Optional<T> getOptionalDependency(String name) {
        T dependency = (T) Bukkit.getPluginManager().getPlugin(name);
        return Optional.ofNullable(dependency);
    }

    private Injector register() {
        YukikaseLib lib = (YukikaseLib) Bukkit.getPluginManager().getPlugin("YukikaseLib");

        if (lib == null) {
            getLogger().log(Level.SEVERE, "Missing dependency: YukikaseLib");
            Bukkit.getPluginManager().disablePlugin(this);
            return null;
        }

        return lib.registerPlugin(this);
    }

    public boolean databaseEnabled() {
        return false;
    }

    public final void startTimer(Timer timer) {
        var runnable = new BukkitRunnable() {
            @Override
            public void run() {
                timer.run();
                if (timer.isCancelled()) {
                    cancel();
                    runningTimers.remove(timer);
                }
            }
        }.runTaskTimer(this, timer.getDelay(), timer.getInterval());

        runningTimers.put(timer, runnable);
    }

    public final void startTimerAsync(Timer timer) {
        var runnable = new BukkitRunnable() {
            @Override
            public void run() {
                timer.run();
                if (timer.isCancelled()) {
                    cancel();
                    runningTimers.remove(timer);
                }
            }
        }.runTaskTimerAsynchronously(this, timer.getDelay(), timer.getInterval());

        runningTimers.put(timer, runnable);
    }

    public final void startTask(Task task) {
        var runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
                if (task.isCancelled()) {
                    task.onCancel();
                    cancel();
                }
            }
        }.runTaskLater(this, task.getDelay());

        runningTasks.put(task, runnable);
    }

    public final void startTaskAsync(Task task) {
        var runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
                if (task.isCancelled()) {
                    task.onCancel();
                    cancel();
                }
            }
        }.runTaskLaterAsynchronously(this, task.getDelay());

        runningTasks.put(task, runnable);
    }

    public final <T> T getInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    public final void writePacket(PacketEvent event) {
        var manager = ProtocolLibrary.getProtocolManager();
        var packet = manager.createPacket(event.getPacketType());
        packet = event.write(packet);
        manager.sendServerPacket(event.player(), packet);
    }
}
