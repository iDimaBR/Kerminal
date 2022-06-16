package com.github.kerminal.commands;


import com.github.kerminal.Kerminal;
import com.github.kerminal.utils.ConfigUtil;
import com.github.kerminal.utils.TimeUtils;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.CommandInfo;
import me.saiintbrisson.minecraft.command.command.Context;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;

@AllArgsConstructor
public class InfolagCommand {

    private Kerminal plugin;
    private ConfigUtil commands;

    public InfolagCommand(Kerminal plugin) {
        this.plugin = plugin;
        this.commands = plugin.getCommands();
        if(!commands.getBoolean("Infolag.enabled", true)) return;
        plugin.getBukkitFrame().registerCommand(
                CommandInfo.builder()
                        .name(commands.getString("Infolag.command"))
                        .aliases(commands.getStringList("Infolag.aliases").toArray(new String[0]))
                        .permission(commands.getString("Infolag.permission"))
                        .async(commands.getBoolean("Infolag.async"))
                        .build(),
                context -> {
                    onCommand(context, context.getArg(0));
                    return false;
                }
        );
    }

    public void onCommand(Context<CommandSender> context, @Optional String target) {
        final CommandSender sender = context.getSender();

        final long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        final long maxMemory = Runtime.getRuntime().maxMemory();
        final long freeMemory = Runtime.getRuntime().freeMemory();
        final int processors = Runtime.getRuntime().availableProcessors();

        final double[] recentTps = MinecraftServer.getServer().recentTps;
        final String tps1 = String.format("%.2f", recentTps[0]).replace(",",".");
        final String tps2 = String.format("%.2f", recentTps[1]).replace(",",".");
        final String tps3 = String.format("%.2f", recentTps[2]).replace(",",".");

        final int sizeOfRunnables = MinecraftServer.getServer().processQueue.size() + Bukkit.getScheduler().getActiveWorkers().size()  + Bukkit.getScheduler().getPendingTasks().size();

        final double lagOfTPS = Math.round((1.0D - recentTps[0] / 20.0D) * 100.0D);

        final String jarType = Bukkit.getVersion().split("git-")[1].split("-")[0];
        final String version = Bukkit.getVersion().split("MC: ")[1].split("\\)")[0];;

        sender.sendMessage("");
        sender.sendMessage("§a Informações de desempenho");
        sender.sendMessage("");
        sender.sendMessage("  §7Tempo de Execução: §f" + TimeUtils.format(uptime));
        sender.sendMessage("  §7Versão do servidor: §f" + jarType + " " + version);
        sender.sendMessage("  §7Núcleos do Processador: §f" + processors);
        sender.sendMessage("  §7Memória: §f" + formatSize(maxMemory - freeMemory) + "/" + formatSize(maxMemory));
        sender.sendMessage("  §7TPS (1m, 5m, 15): §f" + tps1 + ", " + tps2 + ", " + tps3 + " [ " + lagOfTPS + "% ]");
        sender.sendMessage("  §7Tasks: §f" + sizeOfRunnables);
        sender.sendMessage("");

        List<World> listWorlds = Bukkit.getWorlds();
        sender.sendMessage("  §aLista de Mundos (" + listWorlds.size() + ")");
        sender.sendMessage("");
        for (World world : listWorlds){
            sender.sendMessage("  §7- §f" + world.getName() + "§7 (Chunks: §f" + world.getLoadedChunks().length + "§7, Entidades: §f" + world.getLivingEntities().size() + "§7)");
        }
        sender.sendMessage("");
    }
    private String formatSize(long v) {
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double)v / (1L << (z*10)), " KMGTPE".charAt(z));
    }

}
