package me.TylerDW.VoltCut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@SuppressWarnings("unused")
public class VoltCut extends JavaPlugin implements Listener {

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " has been disabled");

		saveConfig();

		getServer().clearRecipes();
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(ChatColor.AQUA + "'" + ChatColor.GREEN
				+ pdfFile.getName() + ChatColor.AQUA + "'"
				+ " has been enabled!");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);

		cred[0] = ChatColor.DARK_GREEN
				+ "\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592";
		cred[1] = ChatColor.DARK_GREEN + "\u2592" + ChatColor.GREEN
				+ "\u2592\u2592\u2592\u2592\u2592\u2592" + ChatColor.DARK_GREEN
				+ "\u2592";
		cred[2] = ChatColor.DARK_GREEN + "\u2592" + ChatColor.GREEN
				+ "\u2592\u2592\u2592\u2592\u2592\u2592" + ChatColor.DARK_GREEN
				+ "\u2592";
		cred[3] = ChatColor.WHITE + "\u2592\u2592\u2592" + ChatColor.GREEN
				+ "\u2592\u2592" + ChatColor.WHITE + "\u2592\u2592\u2592";
		cred[4] = ChatColor.WHITE + "\u2592" + ChatColor.BLACK + "\u2592"
				+ ChatColor.WHITE + "\u2592" + ChatColor.GREEN + "\u2592\u2592"
				+ ChatColor.WHITE + "\u2592" + ChatColor.BLACK + "\u2592"
				+ ChatColor.WHITE + "\u2592";
		cred[5] = ChatColor.WHITE + "\u2592\u2592\u2592" + ChatColor.GREEN
				+ "\u2592\u2592" + ChatColor.WHITE + "\u2592\u2592\u2592";
		cred[6] = ChatColor.DARK_GREEN + "\u2592" + ChatColor.GREEN
				+ "\u2592\u2592\u2592\u2592\u2592\u2592" + ChatColor.DARK_GREEN
				+ "\u2592";
		cred[7] = ChatColor.DARK_GREEN + "\u2592\u2592\u2592\u2592\u2592"
				+ ChatColor.BLACK + "\u2592" + ChatColor.DARK_GREEN
				+ "\u2592\u2592";

		Effects.add("Heal");
		Effects.add("Feed");
		Effects.add("Praise");
		Effects.add("Suicide");
		Effects.add("Poison");

		crateBlacklist.add(Material.CAULDRON);
		crateBlacklist.add(Material.HOPPER);

		// SHOP ITEMS

		ItemMeta jpm = JumpPad.getItemMeta();
		jpm.setDisplayName(ChatColor.AQUA + "Jump Pad");
		ArrayList<String> jpl = new ArrayList<String>();
		jpl.add(ChatColor.GREEN + "Jump high! $150 (1 use only)!");
		jpm.setLore(jpl);
		JumpPad.setItemMeta(jpm);

		ItemMeta inm = InstaKill.getItemMeta();
		inm.setDisplayName(ChatColor.DARK_RED + "InstaKill");
		ArrayList<String> inl = new ArrayList<String>();
		inl.add(ChatColor.RED + "Kill with one swift punch! $500 (1 use only)!");
		inm.setLore(inl);
		InstaKill.setItemMeta(inm);

		ItemMeta djm = DoubleJump.getItemMeta();
		djm.setDisplayName(ChatColor.GREEN + "Double Jump");
		ArrayList<String> djl = new ArrayList<String>();
		djl.add(ChatColor.AQUA + "Jump, twice! $200 (1 use only)");
		djm.setLore(djl);
		DoubleJump.setItemMeta(djm);

		ItemMeta fm = Final.getItemMeta();
		fm.setDisplayName(ChatColor.BLUE + "Final Stand");
		ArrayList<String> fl = new ArrayList<String>();
		fl.add(ChatColor.DARK_AQUA + "Hang on for dear life! $750 (1 use only)");
		fm.setLore(fl);
		Final.setItemMeta(fm);

		ItemMeta fim = Fire.getItemMeta();
		fim.setDisplayName(ChatColor.RED + "Fire Touch");
		ArrayList<String> fil = new ArrayList<String>();
		fil.add(ChatColor.DARK_RED + "Fire with each hit! $100 (5 uses)");
		fim.setLore(fil);
		Fire.setItemMeta(fim);

		ItemMeta pom = Poison.getItemMeta();
		pom.setDisplayName(ChatColor.DARK_GREEN + "Poison Touch");
		ArrayList<String> pol = new ArrayList<String>();
		pol.add(ChatColor.DARK_PURPLE + "Poison with each hit! $100 (5 uses)");
		pom.setLore(pol);
		Poison.setItemMeta(pom);

		ItemMeta sam = ShockAbsorb.getItemMeta();
		sam.setDisplayName(ChatColor.GOLD + "Shock Absorber");
		ArrayList<String> sal = new ArrayList<String>();
		sal.add(ChatColor.GRAY + "No fall damage! $400 (5 uses)");
		sam.setLore(sal);
		ShockAbsorb.setItemMeta(sam);

		// EXTRAS

		ItemMeta empm = Empty.getItemMeta();
		empm.setDisplayName(ChatColor.GRAY + "Empty");
		ArrayList<String> empl = new ArrayList<String>();
		empl.add(ChatColor.GRAY + "(Pointless)");
		empm.setLore(empl);
		Empty.setItemMeta(empm);

		ItemMeta adm = Advert.getItemMeta();
		adm.setDisplayName(ChatColor.GREEN + "Credit!");
		ArrayList<String> adl = new ArrayList<String>();
		adl.add(ChatColor.AQUA + "Plugin created by Tyler! :D");
		adm.setLore(adl);
		Advert.setItemMeta(adm);

		ItemMeta clm = Clear.getItemMeta();
		clm.setDisplayName(ChatColor.WHITE + "Clear Ability?");
		ArrayList<String> cll = new ArrayList<String>();
		cll.add(ChatColor.GRAY + "Clear your current ability (Unlimited)");
		clm.setLore(cll);
		Clear.setItemMeta(clm);

		ItemMeta infm = Info.getItemMeta();
		infm.setDisplayName(ChatColor.WHITE + "Info!");
		ArrayList<String> infl = new ArrayList<String>();
		infl.add(ChatColor.WHITE
				+ "Earn money, buy items and use /vcability to use 'em!");
		infm.setLore(infl);
		Info.setItemMeta(infm);

		// ABILITY ITEMS

		ItemMeta ajpm = aJumpPad.getItemMeta();
		ajpm.setDisplayName(ChatColor.AQUA + "Jump Pad");
		ArrayList<String> ajpl = new ArrayList<String>();
		ajpl.add(ChatColor.GREEN + "Jump high! Sneak while jumping!");
		ajpm.setLore(ajpl);
		aJumpPad.setItemMeta(ajpm);

		ItemMeta ainm = aInstaKill.getItemMeta();
		ainm.setDisplayName(ChatColor.DARK_RED + "InstaKill");
		ArrayList<String> ainl = new ArrayList<String>();
		ainl.add(ChatColor.RED + "Kill! Punch with your hand!");
		ainm.setLore(ainl);
		aInstaKill.setItemMeta(ainm);

		ItemMeta adjm = aDoubleJump.getItemMeta();
		adjm.setDisplayName(ChatColor.GREEN + "Double Jump");
		ArrayList<String> adjl = new ArrayList<String>();
		adjl.add(ChatColor.AQUA + "Jump, twice! Sneak while jumping!");
		adjm.setLore(adjl);
		aDoubleJump.setItemMeta(adjm);

		ItemMeta afm = aFinal.getItemMeta();
		afm.setDisplayName(ChatColor.BLUE + "Final Stand");
		ArrayList<String> afl = new ArrayList<String>();
		afl.add(ChatColor.DARK_AQUA + "Stay on half a heart!");
		afm.setLore(afl);
		aFinal.setItemMeta(afm);

		ItemMeta afim = aFire.getItemMeta();
		afim.setDisplayName(ChatColor.RED + "Fire Touch");
		ArrayList<String> afil = new ArrayList<String>();
		afil.add(ChatColor.DARK_RED + "Fire! Punch with your hand!");
		afim.setLore(afil);
		aFire.setItemMeta(afim);

		ItemMeta apom = aPoison.getItemMeta();
		apom.setDisplayName(ChatColor.DARK_GREEN + "Poison Touch");
		ArrayList<String> apol = new ArrayList<String>();
		apol.add(ChatColor.DARK_PURPLE + "Poison! Punch with your hand!");
		apom.setLore(apol);
		aPoison.setItemMeta(apom);

		ItemMeta asam = aShockAbsorb.getItemMeta();
		asam.setDisplayName(ChatColor.GOLD + "Shock Absorber");
		ArrayList<String> asal = new ArrayList<String>();
		asal.add(ChatColor.GRAY + "No fall damage!");
		asam.setLore(asal);
		aShockAbsorb.setItemMeta(asam);

		shop = Bukkit.createInventory(null, 9, "VoltCut Store");
		ability = Bukkit.createInventory(null, 9, "VoltCut Abilities");

		blacklist.add(EntityType.ENDER_DRAGON);
		blacklist.add(EntityType.GIANT);
		blacklist.add(EntityType.IRON_GOLEM);
		blacklist.add(EntityType.WITHER);
		blacklist.add(EntityType.PLAYER);

		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	ArrayList<Player> jumped = new ArrayList<Player>();
	ArrayList<Player> jumpedDmg = new ArrayList<Player>();
	ArrayList<EntityType> blacklist = new ArrayList<EntityType>();
	ArrayList<Player> FSC = new ArrayList<Player>();
	ArrayList<Material> crateBlacklist = new ArrayList<Material>();

	static String[] cred = new String[] { "", "", "", "", "", "", "", "" };

	static ArrayList<Integer> timeID = new ArrayList<Integer>();

	org.bukkit.Server Server = Bukkit.getServer();

	static Inventory shop;
	static Inventory ability;

	// SHOP ITEMS

	static ItemStack JumpPad = new ItemStack(Material.FEATHER);
	static ItemStack ShockAbsorb = new ItemStack(Material.WOOL);
	static ItemStack InstaKill = new ItemStack(Material.BLAZE_ROD);
	static ItemStack DoubleJump = new ItemStack(Material.ARROW);
	static ItemStack Final = new ItemStack(Material.SNOW_BALL);
	static ItemStack Fire = new ItemStack(Material.FIRE);
	static ItemStack Poison = new ItemStack(Material.FERMENTED_SPIDER_EYE);

	// EXTRAS

	static ItemStack Clear = new ItemStack(Material.PORTAL);
	static ItemStack Empty = new ItemStack(Material.MONSTER_EGG);
	static ItemStack Advert = new ItemStack(Material.SLIME_BALL);
	static ItemStack Info = new ItemStack(Material.BOOK_AND_QUILL);

	// ABILITY ITEMS

	static ItemStack aJumpPad = new ItemStack(Material.FEATHER); // DONE
	static ItemStack aShockAbsorb = new ItemStack(Material.WOOL); // DONE
	static ItemStack aInstaKill = new ItemStack(Material.BLAZE_ROD); // DONE
	static ItemStack aDoubleJump = new ItemStack(Material.ARROW); // DONE
	static ItemStack aFinal = new ItemStack(Material.SNOW_BALL); // DONE
	static ItemStack aFire = new ItemStack(Material.FIRE);
	static ItemStack aPoison = new ItemStack(Material.FERMENTED_SPIDER_EYE);

	ArrayList<String> Effects = new ArrayList<String>();
	ArrayList<Player> noPickup = new ArrayList<Player>();

	String argsMsg = ChatColor.RED + "[" + ChatColor.AQUA + "VoltCut"
			+ ChatColor.RED + "] Not the right amount of arguments!";
	String existMsg = ChatColor.RED + "[" + ChatColor.AQUA + "VoltCut"
			+ ChatColor.RED + "] That player doesn't exist!";
	String permMsg = ChatColor.RED + "[" + ChatColor.AQUA + "VoltCut"
			+ ChatColor.RED + "] You don't have the permissions!";
	String monMsg = ChatColor.RED + "[" + ChatColor.AQUA + "VoltCut"
			+ ChatColor.RED + "] You don't have the money!";

	public final Logger logger = Logger.getLogger("Minecraft");
	public static VoltCut plugin;

	@EventHandler
	public void KillEarn(EntityDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			Player killer = (Player) e.getEntity().getKiller();
			boolean passive;
			boolean hostile;
			boolean player;
			boolean spam;
			passive = getConfig().getBoolean("moneyEarnPassiveKill");
			hostile = getConfig().getBoolean("moneyEarnHostileKill");
			player = getConfig().getBoolean("moneyEarnPlayerKill");
			spam = getConfig().getBoolean(killer.getName() + ".spam");
			if (e.getEntity() instanceof Player) {
				if (player) {
					if (!spam) {
						killer.sendMessage(ChatColor.DARK_RED + "Player kill!");
					}
					moneyAdd(25, killer, true);
				}
			} else {
				if (e.getEntity() instanceof Monster) {
					if (hostile) {
						if (!spam) {
							killer.sendMessage(ChatColor.DARK_RED
									+ "Monster kill!");
						}
						moneyAdd(35, killer, true);
					}
				} else {
					if (passive) {
						if (!spam) {
							killer.sendMessage(ChatColor.DARK_RED
									+ "Passive kill!");
						}
						moneyAdd(15, killer, true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onMineOre(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Material m = e.getBlock().getType();
		if (getConfig().getBoolean("moneyEarnOre")) {
			if (p.getGameMode() != GameMode.CREATIVE) {
				boolean spam = getConfig().getBoolean(p.getName() + ".spam");
				if (m == Material.COAL_ORE) {
					if (!spam) {
						p.sendMessage(ChatColor.BLACK + "Coal!");
					}
					moneyAdd(5, p, true);
				}
				if (m == Material.REDSTONE_ORE) {
					if (!spam) {
						p.sendMessage(ChatColor.RED + "Redstone!");
					}
					moneyAdd(10, p, true);
				}
				if (m == Material.EMERALD_ORE) {
					if (!spam) {
						p.sendMessage(ChatColor.GREEN + "Emerald!");
					}
					moneyAdd(25, p, true);
				}
				if (m == Material.DIAMOND_ORE) {
					if (!spam) {
						p.sendMessage(ChatColor.AQUA + "Diamond!");
					}
					moneyAdd(30, p, true);
				}
				if (m == Material.QUARTZ_ORE) {
					if (!spam) {
						p.sendMessage(ChatColor.GRAY + "Quartz!");
					}
					moneyAdd(15, p, true);
				}
				if (m == Material.LAPIS_ORE) {
					if (!spam) {
						p.sendMessage(ChatColor.BLUE + "Lapis!");
					}
					moneyAdd(20, p, true);
				}
			}
			saveConfig();
		}
	}

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		boolean spam = getConfig().getBoolean(p.getName() + ".spam");
		if (getConfig().getInt(p.getName() + ".currentAbility") == 4) {
			int a = abilityAmt(4, p);
			if (p.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					if (a > 0) {
						if (!jumped.contains(p)) {
							Vector v = p.getLocation().getDirection()
									.multiply(1).setY(1);
							p.setVelocity(v);
							if (!spam) {
								p.sendMessage(ChatColor.AQUA
										+ "A gust of wind pushes you upward!");
							}
							removeOne(4, p);
							jumped.add(p);
							jumpedDmg.add(p);
							p.setFallDistance((float) 4);
						}
					} else {
						if (!spam) {
							p.sendMessage(ChatColor.RED
									+ "You have run out of this ability!");
						}
					}
				}
			}
		}
		if (getConfig().getInt(p.getName() + ".currentAbility") == 1) {
			int a = abilityAmt(1, p);
			if (p.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR) {
				if (p.getGameMode() != GameMode.CREATIVE) {
					if (a > 0) {
						if (!jumped.contains(p)) {
							p.setVelocity(new Vector(0, 1, 0));
							if (!spam) {
								p.sendMessage(ChatColor.AQUA
										+ "You fly upwards!");
							}
							removeOne(1, p);
							jumped.add(p);
							jumpedDmg.add(p);
							p.setFallDistance((float) 4);
						}
					} else {
						if (!spam) {
							p.sendMessage(ChatColor.RED
									+ "You have run out of this ability!");
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.hasPlayedBefore() == false) {
			p.sendMessage(ChatColor.AQUA
					+ "Welcome! For your first visit to this server you get $150, for free!");
		}
		saveConfig();
		configger(p);
		p.sendMessage(ChatColor.AQUA + "You have '" + ChatColor.GREEN
				+ getMoneyS(p) + ChatColor.AQUA
				+ "' VoltCut Dollars! Use /vcshop to spend it!");
	}

	@EventHandler
	public void onDmg(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			boolean spam = getConfig().getBoolean(p.getName() + ".spam");
			if (e.getCause() == DamageCause.FALL) {
				if (jumpedDmg.contains(p)) {
					jumpedDmg.remove(p);
					if (getConfig().getBoolean("abilityJumpingDamage") == false) {
						e.setCancelled(true);
					}
				}
				if (getConfig().getInt(p.getName() + ".currentAbility") == 2) {
					int a = abilityAmt(2, p);
					if (a > 0) {
						if (p.getGameMode() != GameMode.CREATIVE) {
							if (!spam) {
								p.sendMessage(ChatColor.AQUA
										+ "You have used a shock absorber!");
							}
							removeOne(2, p);
							e.setCancelled(true);
						}
					} else {
						if (!spam) {
							p.sendMessage(ChatColor.RED
									+ "You have run out of this ability!");
						}
					}
				}
			}
			Damageable d = (Damageable) p;
			double hp = (double) d.getHealth();
			if (hp - e.getDamage() < 1) {
				if (getConfig().getInt(p.getName() + ".currentAbility") == 5) {
					int a = abilityAmt(5, p);
					if (p.getGameMode() != GameMode.CREATIVE) {
						if (!FSC.contains(p)) {
							if (p.isDead() == false) {
								if (a > 0) {
									p.sendMessage(ChatColor.AQUA
											+ "Stay alive!");
									p.setHealth((double) 2);
									removeOne(5, p);
									FSC.add(p);
									FSCcool(p);
									e.setCancelled(true);
								} else {
									FSCcool(p);
									p.sendMessage(ChatColor.RED
											+ "You have run out of this ability!");
								}
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void FSCcool(final Player p) {
		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				FSC.remove(p);
			}
		}, 20);
	}

	@EventHandler
	public void onInstaPoisonFire(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			boolean spam = getConfig().getBoolean(p.getName() + ".spam");
			if (getConfig().getInt(p.getName() + ".currentAbility") == 3) {
				int a = abilityAmt(3, p);
				if (a > 0) {
					if (p.getGameMode() != GameMode.CREATIVE) {
						if (!blacklist.contains(e.getEntity().getType())) {
							if (e.getEntity().isDead() == false) {
								LivingEntity ee = (LivingEntity) e.getEntity();
								ee.setHealth((double) 0);
								if (!spam) {
									p.sendMessage(ChatColor.DARK_RED
											+ "One punch was all you needed...");
								}
								removeOne(3, p);
							}
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(ChatColor.RED
								+ "You have run out of this ability!");
					}
				}
			}
			if (getConfig().getInt(p.getName() + ".currentAbility") == 6) {
				int a = abilityAmt(6, p);
				if (a > 0) {
					if (p.getGameMode() != GameMode.CREATIVE) {
						if (!blacklist.contains(e.getEntity().getType())) {
							if (e.getEntity().isDead() == false) {
								LivingEntity ee = (LivingEntity) e.getEntity();
								ee.setFireTicks(60);
								if (!spam) {
									p.sendMessage(ChatColor.DARK_RED
											+ "Burn! Baby Burn!");
								}
								removeOne(6, p);
							}
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(ChatColor.RED
								+ "You have run out of this ability!");
					}
				}
			}
			if (getConfig().getInt(p.getName() + ".currentAbility") == 7) {
				int a = abilityAmt(7, p);
				if (a > 0) {
					if (p.getGameMode() != GameMode.CREATIVE) {
						if (!blacklist.contains(e.getEntity().getType())) {
							if (e.getEntity().isDead() == false) {
								LivingEntity ee = (LivingEntity) e.getEntity();
								ee.addPotionEffect(new PotionEffect(
										PotionEffectType.POISON, 40, 1));
								if (!spam) {
									p.sendMessage(ChatColor.DARK_GREEN
											+ "The venom seeps into your foe!");
								}
								removeOne(7, p);
							}
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(ChatColor.RED
								+ "You have run out of this ability!");
					}
				}
			}
		}
	}

	@EventHandler
	public void jumpRemove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!(p.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR)) {
			if (jumped.contains(p)) {
				jumped.remove(p);
			}
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if (noPickup.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Material i = Material.AIR;
		try {
			i = e.getCurrentItem().getType();
		} catch (NullPointerException exp) {
			e.setCancelled(true);
		}
		int m = getMoneyI(p);
		int a = 0;
		boolean b = false;
		boolean orr = false;
		boolean spam = getConfig().getBoolean(p.getName() + ".spam");
		if (e.getInventory().getTitle() == "VoltCut Store") {
			if (i == Material.FEATHER) {
				if (m >= 150) {
					getConfig().set(p.getName() + ".money", m - 150);
					b = true;
					a = 1;
				}
			} else if (i == Material.WOOL) {
				if (m >= 400) {
					getConfig().set(p.getName() + ".money", m - 400);
					b = true;
					a = 2;
				}
			} else if (i == Material.BLAZE_ROD) {
				if (m >= 500) {
					getConfig().set(p.getName() + ".money", m - 500);
					b = true;
					a = 3;
				}
			} else if (i == Material.ARROW) {
				if (m >= 200) {
					getConfig().set(p.getName() + ".money", m - 200);
					b = true;
					a = 4;
				}
			} else if (i == Material.SNOW_BALL) {
				if (m >= 750) {
					getConfig().set(p.getName() + ".money", m - 750);
					b = true;
					a = 5;
				}
			} else if (i == Material.FIRE) {
				if (m >= 100) {
					getConfig().set(p.getName() + ".money", m - 100);
					b = true;
					a = 6;
				}
			} else if (i == Material.FERMENTED_SPIDER_EYE) {
				if (m >= 100) {
					getConfig().set(p.getName() + ".money", m - 100);
					b = true;
					a = 7;
				}
			} else if (i == Material.BOOK_AND_QUILL) {
				orr = true;
			} else if (i == Material.SLIME_BALL) {
				orr = true;
			} else {
				orr = true;
			}
			if (orr == true) {
				if (!spam) {
					p.sendMessage(ChatColor.RED + "You can't buy that!");
				}
			} else if (b == true) {
				if (!spam) {
					p.sendMessage(ChatColor.AQUA + "Thanks for buying!");
				}
				abilityAdd(a, p);
			} else {
				if (!spam) {
					p.sendMessage(ChatColor.RED
							+ "You don't have the money for that!");
				}
			}
			((CraftPlayer) p).getHandle().updateInventory(
					((CraftPlayer) p).getHandle().activeContainer);
			saveConfig();
			e.setCancelled(true);
		}
		if (e.getInventory().getTitle() == "VoltCut Abilities") {
			if (i == Material.FEATHER) {
				a = 1;
			} else if (i == Material.WOOL) {
				a = 2;
			} else if (i == Material.BLAZE_ROD) {
				a = 3;
			} else if (i == Material.ARROW) {
				a = 4;
			} else if (i == Material.SNOW_BALL) {
				a = 5;
			} else if (i == Material.FIRE) {
				a = 6;
			} else if (i == Material.FERMENTED_SPIDER_EYE) {
				a = 7;
			} else if (i == Material.BOOK_AND_QUILL) {
				a = 0;
			} else if (i == Material.SLIME_BALL) {
				for (int ii = 0; ii < 8; ii++) {
					p.sendMessage(cred[ii]);
				}
				p.sendMessage(ChatColor.GREEN
						+ "Like my stuff? Fluxanoia on the Bukkit Forums. ;)");
			} else {
				orr = true;
			}
			if (orr == true) {
				if (!spam) {
					p.sendMessage(ChatColor.RED + "You can't select that!");
				}
			} else {
				if (!spam) {
					p.sendMessage(ChatColor.AQUA + "You selected "
							+ ChatColor.GREEN + abilityStr(a) + "!"
							+ " Number : " + a);
				}
				getConfig().set(p.getName() + ".currentAbility", a);
			}
			saveConfig();
			e.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player) {
			final Player p = (Player) sender;
			boolean spam = getConfig().getBoolean(p.getName() + ".spam");
			if (label.equalsIgnoreCase("VCadd")) {
				if (p.hasPermission("voltcut.prefix")) {
					if (args.length >= 1 && args.length <= 4) {
						if (args[0].equalsIgnoreCase("VCNULL")) {
							p.setDisplayName(p.getName());
							if (!spam) {
								p.sendMessage("Your current chat name is ' "
										+ p.getDisplayName() + " '!");
							}
							return false;
						}
						if (args[0].length() > 10) {
							if (!spam) {
								p.sendMessage(ChatColor.RED + "["
										+ ChatColor.AQUA + "VoltCut"
										+ ChatColor.RED
										+ "] The prefix is too long!");
							}
							return false;
						}
						boolean border = false;
						boolean inner = false;
						boolean name = false;
						if (args.length > 3) {
							border = true;
							inner = true;
							name = true;
						} else if (args.length > 2) {
							border = true;
							inner = true;
						} else if (args.length > 1) {
							inner = true;
						}
						ChatColor innerC = ChatColor.WHITE;
						ChatColor borderC = ChatColor.WHITE;
						ChatColor nameC = ChatColor.WHITE;
						if (inner == true) {
							ChatColor newInner = colourListener(args[1]);
							innerC = newInner;
						}
						if (border == true) {
							ChatColor newBorder = colourListener(args[2]);
							borderC = newBorder;
						}
						if (name == true) {
							ChatColor newName = colourListener(args[3]);
							nameC = newName;
						}
						p.setDisplayName(borderC + "[" + innerC + args[0]
								+ borderC + "] " + nameC + p.getName()
								+ ChatColor.RESET);
						if (!spam) {
							p.sendMessage("Your current chat name is ' "
									+ p.getDisplayName() + " '!");
						}
					} else {
						if (!spam) {
							p.sendMessage(argsMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(permMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("VCsetmaxhealth")) {
				if (args.length == 1) {
					if (p.hasPermission("voltcut.maxhealth")) {
						String s = args[0];
						double d = Double.valueOf(s);
						d = Math.round(d);
						if (d < 21 && d > 0) {
							p.setMaxHealth(d);
							p.setHealth(d);
							if (!spam) {
								p.sendMessage("You now have " + ChatColor.AQUA
										+ d / 2 + ChatColor.RESET + " hearts!");
							}
						} else {
							if (!spam) {
								p.sendMessage(ChatColor.RED
										+ "["
										+ ChatColor.AQUA
										+ "VoltCut"
										+ ChatColor.RED
										+ "] Input a number below 21 and above 0!");
							}
						}
					} else {
						if (!spam) {
							p.sendMessage(permMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(argsMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("VCeffect")) {
				if (args.length == 1 || args.length == 2) {
					if (p.hasPermission("voltcut.effect")) {
						if (effectContains(args[0]) == true) {
							String ee = args[0];
							Player ep = null;
							if (args.length == 2) {
								ep = Bukkit.getPlayer(args[1]);
								if (ep == null) {
									if (!spam) {
										p.sendMessage(existMsg);
									}
								}
							} else {
								ep = p;
							}
							String ef = args[0];
							String first = ef.substring(0, 1);
							String last = ef.substring(1);
							ef = first.toUpperCase() + last.toLowerCase();
							if (ee.equalsIgnoreCase("heal")) {
								ep.setHealth((double) 20);
							}
							if (ee.equalsIgnoreCase("feed")) {
								ep.setFoodLevel(20);
								ep.setSaturation(20);
							}
							if (ee.equalsIgnoreCase("praise")) {
								ep.setFoodLevel(20);
								ep.setSaturation(20);
								ep.setHealth((double) 20);
							}
							if (ee.equalsIgnoreCase("suicide")) {
								ep.setHealth((double) 0);
							}
							if (ee.equalsIgnoreCase("poison")) {
								ep.addPotionEffect(new PotionEffect(
										PotionEffectType.POISON, 100, 1));
							}
							if (!spam) {
								p.sendMessage(ChatColor.AQUA
										+ "You have just used the effect, '"
										+ ChatColor.GREEN + ef + ChatColor.AQUA
										+ "'!");
							}
							Firework fw = (Firework) p.getWorld().spawn(
									p.getLocation(), Firework.class);
							FireworkMeta fm = fw.getFireworkMeta();
							Type t = Type.BALL_LARGE;
							Color c = Color.AQUA;
							FireworkEffect fe = FireworkEffect.builder()
									.flicker(true).trail(true).withColor(c)
									.with(t).build();
							fm.addEffect(fe);
							Random ran = new Random();
							int i = ran.nextInt(4);
							fm.setPower(i);
							fw.setFireworkMeta(fm);
						} else {
							if (!spam) {
								p.sendMessage(ChatColor.RED + "["
										+ ChatColor.AQUA + "VoltCut"
										+ ChatColor.RED
										+ "] That's not an effect!");
							}
						}
					} else {
						if (!spam) {
							p.sendMessage(permMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(argsMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("VCeffect-types")) {
				if (args.length == 0) {
					if (p.hasPermission("voltcut.effecttypes")) {
						Iterator<String> itr = Effects.iterator();
							p.sendMessage(ChatColor.AQUA
									+ "These are the effects:");
						while (itr.hasNext()) {
								p.sendMessage(ChatColor.AQUA + "'"
										+ ChatColor.GREEN + itr.next()
										+ ChatColor.AQUA + "'");
							}
						if (!spam) {
							p.sendMessage(ChatColor.AQUA
									+ "Use them with moderation!");
						}
					} else {
						if (!spam) {
							p.sendMessage(permMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(argsMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("VCability-numbers")) {
				if (args.length == 0) {
					if (p.hasPermission("voltcut.abilitynumbers")) {
						if (!spam) {
							p.sendMessage(ChatColor.AQUA
									+ "These are the ability numbers:");
						}
						for (int i = 1; i < 8; i++) {
							if (!spam) {
								p.sendMessage(ChatColor.AQUA + abilityStr(i)
										+ " '" + ChatColor.GREEN
										+ abilityAmt(i, p) + ChatColor.AQUA
										+ "'");
							}
						}
						if (!spam) {
							p.sendMessage(ChatColor.AQUA
									+ "Use them with moderation!");
						}
					} else {
						if (!spam) {
							p.sendMessage(permMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(argsMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("VCability-types")) {
				if (args.length == 0) {
					if (p.hasPermission("voltcut.abilitytypes")) {
						Iterator<String> itr = Effects.iterator();
							p.sendMessage(ChatColor.AQUA
									+ "These are the abilities:");
						while (itr.hasNext()) {
								p.sendMessage(ChatColor.AQUA + "'"
										+ ChatColor.GREEN + itr.next()
										+ ChatColor.AQUA + "'");
							
						}
						if (!spam) {
							p.sendMessage(ChatColor.AQUA
									+ "Use them with moderation!");
						}
					} else {
						if (!spam) {
							p.sendMessage(permMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(argsMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("VCexplode")) {
				if (p.hasPermission("voltcut.explode")) {
					if (args.length == 0 || args.length == 1) {
						Player obl = null;
						if (args.length == 1) {
							obl = Bukkit.getPlayer(args[0]);
						}
						if (obl == null) {
							obl = p;
						}
						if (args.length == 0) {
							obl = p;
						}
						obl.setHealth((double) 0);
						Location loc = obl.getLocation();
						obl.getWorld().createExplosion(loc.getBlockX(),
								loc.getBlockY(), loc.getBlockZ(), 10, false,
								false);
					} else {
						if (!spam) {
							p.sendMessage(argsMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(permMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("VCfirework")) {
				if (p.hasPermission("voltcut.firework")) {
					if (args.length == 0 || args.length == 1) {
						Player obl = null;
						if (args.length == 1) {
							obl = Bukkit.getPlayer(args[0]);
						}
						if (obl == null) {
							obl = p;
						}
						if (args.length == 0) {
							obl = p;
						}
						if (!spam) {
							obl.sendMessage(ChatColor.AQUA
									+ "Are you ready to be a "
									+ ChatColor.GREEN + "firework"
									+ ChatColor.AQUA + "?");
						}
						obl.setVelocity(new Vector(0, 5, 0));
						Location loc = obl.getLocation();
						obl.getWorld().spawnEntity(loc, EntityType.FIREWORK);
						final Player pe = obl;
						Bukkit.getScheduler().scheduleSyncDelayedTask(this,
								new Runnable() {
									@Override
									public void run() {
										pe.setHealth((double) 0);
										Location loc = pe.getLocation();
										pe.getWorld().createExplosion(
												loc.getBlockX(),
												loc.getBlockY(),
												loc.getBlockZ(), 10, false,
												false);
										Server.broadcastMessage(ChatColor.AQUA
												+ "A firework has been fired. A human one.");
									}
								}, 40);
					}
				}
			}
			if (label.equalsIgnoreCase("vcpickup")) {
				if (args.length == 0) {
					if (p.hasPermission("voltcut.pickup")) {
						boolean b = (noPickup.contains(p)) ? true : false;
						if (b == true) {
							noPickup.remove(p);
							if (!spam) {
								p.sendMessage(ChatColor.AQUA + "You've been "
										+ ChatColor.GREEN + "removed "
										+ ChatColor.AQUA
										+ "to the noPickup playlist!");
							}
						} else {
							noPickup.add(p);
							if (!spam) {
								p.sendMessage(ChatColor.AQUA + "You've been "
										+ ChatColor.GREEN + "added "
										+ ChatColor.AQUA
										+ "to the noPickup playlist!");
							}
						}
					} else {
						if (!spam) {
							p.sendMessage(permMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(argsMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("vcmoney")) {
				if (args.length == 0) {
					if (p.hasPermission("voltcut.money")) {
						if (!spam) {
							p.sendMessage(ChatColor.AQUA
									+ "You have '"
									+ ChatColor.GREEN
									+ getMoneyS(p)
									+ ChatColor.AQUA
									+ "' VoltCut Dollars! Use /vcshop to spend it!");
						}
					} else {
						if (!spam) {
							p.sendMessage(permMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(argsMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("vcmoneyset")) {
				if (args.length == 1) {
					if (p.hasPermission("voltcut.moneyset")) {
						int i = 0;
						try {
							i = Integer.valueOf(args[0]);
						} catch (NumberFormatException e) {
							if (!spam) {
								p.sendMessage(ChatColor.RED + "["
										+ ChatColor.AQUA + "VoltCut"
										+ ChatColor.RED + "] Input a number!");
							}
							return false;
						}
						getConfig().set(p.getName() + ".money", i);
						saveConfig();
						if (!spam) {
							p.sendMessage(ChatColor.AQUA
									+ "You have '"
									+ ChatColor.GREEN
									+ getMoneyS(p)
									+ ChatColor.AQUA
									+ "' VoltCut Dollars! Use /vcshop to spend it!");
						}
					} else {
						if (!spam) {
							p.sendMessage(permMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(argsMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("vcshop")) {
				if (args.length == 0) {
					if (p.hasPermission("voltcut.shop")) {
						shop.setItem(0, Info);
						shop.setItem(1, JumpPad);
						shop.setItem(2, ShockAbsorb);
						shop.setItem(3, InstaKill);
						shop.setItem(4, DoubleJump);
						shop.setItem(5, Final);
						shop.setItem(6, Fire);
						shop.setItem(7, Poison);
						shop.setItem(8, Advert);
						p.openInventory(shop);
					} else {
						if (!spam) {
							p.sendMessage(permMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(argsMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("vcability")) {
				if (args.length == 0) {
					ItemStack i = new ItemStack(Material.BOOK_AND_QUILL);
					ItemMeta im = i.getItemMeta();
					im.setDisplayName(ChatColor.WHITE + "Info!");
					ArrayList<String> il = new ArrayList<String>();
					il.add(ChatColor.WHITE + "Pick a ability to equip!");
					im.setLore(il);
					i.setItemMeta(im);
					if (p.hasPermission("voltcut.ability")) {
						ability.setItem(0, i);
						ability.setItem(1, aJumpPad);
						ability.setItem(2, aShockAbsorb);
						ability.setItem(3, aInstaKill);
						ability.setItem(4, aDoubleJump);
						ability.setItem(5, aFinal);
						ability.setItem(6, aFire);
						ability.setItem(7, aPoison);
						ability.setItem(8, Advert);
						p.openInventory(ability);
					} else {
						if (!spam) {
							p.sendMessage(permMsg);
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(argsMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("vcbankreset")) {
				if (p.hasPermission("voltcut.bankreset")) {
					if (args.length == 0 || args.length == 1) {
						Player pp = null;
						if (args.length == 1) {
							pp = Bukkit.getPlayer(args[0]);
						} else {
							pp = p;
						}
						if (pp == null) {
							pp = p;
						}
						getConfig().set(pp.getName() + ".money", 0);
						saveConfig();
						if (!spam) {
							p.sendMessage(ChatColor.AQUA
									+ "You have '"
									+ ChatColor.GREEN
									+ getMoneyS(p)
									+ ChatColor.AQUA
									+ "' VoltCut Dollars! Use /vcshop to spend it!");
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(permMsg);
					}
				}
			}
			if (label.equalsIgnoreCase("VCcrate")) {
				if (p.getLocation().getBlock().getType().equals(Material.AIR)) {
					if (!crateBlacklist.contains(p.getLocation()
							.subtract(0, 1, 0).getBlock().getType())) {
						final int x = p.getLocation().getBlockX();
						final int z = p.getLocation().getBlockZ();
						final World w = p.getWorld();
						final Block bl = w.getHighestBlockAt(x, z);
						final Block below = bl.getLocation().subtract(0,1,0).getBlock();
						final Material oldBelowMaterial = below.getType();
						final Location l = new Location(w, x, 255, z);
						final FallingBlock fb = w.spawnFallingBlock(l,
								Material.WOOD, (byte) 0);
						final int line = timeID.size();
						timeID.add(Bukkit.getScheduler()
								.scheduleAsyncRepeatingTask(this,
										new Runnable() {
											@Override
											public void run() {
												Location loc = new Location(w, x, 255, z);
												if (below.getType() == Material.AIR) {
													below.setType(oldBelowMaterial);
												}
												
												if (bl.getType() == Material.WOOD) {
													bl.setType(Material.CHEST);
													Chest c = (Chest) bl
															.getState();
													ItemStack[] it = p
															.getEnderChest()
															.getContents();
													c.getInventory()
															.setContents(it);
													p.sendMessage(ChatColor.AQUA + "The crate has landed!");
													Bukkit.getScheduler()
															.cancelTask(
																	timeID.get(line));
												}
											}
										}, 0, 20));
					} else {
						if (!spam) {
							p.sendMessage(ChatColor.RED + "[" + ChatColor.AQUA
									+ "VoltCut" + ChatColor.RED
									+ "] Stand in a air block!");
						}
					}
				} else {
					if (!spam) {
						p.sendMessage(ChatColor.RED + "[" + ChatColor.AQUA
								+ "VoltCut" + ChatColor.RED
								+ "] Stand in a air block!");
					}
				}
			}

			if (label.equalsIgnoreCase("vcmessages")) {
				if (p.hasPermission("volcut.spam")) {
					if (args.length == 0) {
						boolean b = (getConfig().getBoolean(p.getName()
								+ ".spam")) ? false : true;
						getConfig().set(p.getName() + ".spam", b);
						p.sendMessage(ChatColor.AQUA + "Your spam is set to '"
								+ ChatColor.GREEN + Boolean.toString(b)
								+ ChatColor.AQUA + "'!");
						saveConfig();
					} else {
						p.sendMessage(argsMsg);
					}
				} else {
					p.sendMessage(permMsg);
				}
			}
		}
		return false;
	}

	public boolean effectContains(String s) {
		boolean b = false;
		Iterator<String> itr = Effects.iterator();
		while (itr.hasNext()) {
			if (itr.next().equalsIgnoreCase(s)) {
				b = true;
			}
		}
		return b;
	}

	public void moneyAdd(int i, Player p, boolean MSG) {
		int money = getMoneyI(p);
		boolean spam = getConfig().getBoolean(p.getName() + ".spam");
		getConfig().set(p.getName() + ".money", money + i);
		saveConfig();
		if (MSG) {
			if (!spam) {
				p.sendMessage(ChatColor.AQUA + "You have '" + ChatColor.GREEN
						+ getMoneyS(p) + ChatColor.AQUA
						+ "' VoltCut Dollars! Use /vcshop to spend it!");
			}
		}
	}

	public String getMoneyS(Player p) {
		String cash = "";
		int rawInt = getConfig().getInt(p.getName() + ".money");
		cash = "$" + rawInt;
		return cash;
	}

	public int getMoneyI(Player p) {
		int cash = getConfig().getInt(p.getName() + ".money");
		return cash;
	}

	public void abilityAdd(int i, Player p) {
		if (i == 1) {
			int ia = getConfig().getInt(p.getName() + ".jump");
			getConfig().set(p.getName() + ".jump", ia + 1);
		}
		if (i == 2) {
			int ia = getConfig().getInt(p.getName() + ".shock");
			getConfig().set(p.getName() + ".shock", ia + 5);
		}
		if (i == 3) {
			int ia = getConfig().getInt(p.getName() + ".insta");
			getConfig().set(p.getName() + ".insta", ia + 1);
		}
		if (i == 4) {
			int ia = getConfig().getInt(p.getName() + ".double");
			getConfig().set(p.getName() + ".double", ia + 1);
		}
		if (i == 5) {
			int ia = getConfig().getInt(p.getName() + ".final");
			getConfig().set(p.getName() + ".final", ia + 1);
		}
		if (i == 6) {
			int ia = getConfig().getInt(p.getName() + ".fire");
			getConfig().set(p.getName() + ".fire", ia + 5);
		}
		if (i == 7) {
			int ia = getConfig().getInt(p.getName() + ".poison");
			getConfig().set(p.getName() + ".poison", ia + 5);
		}
		saveConfig();
	}

	public int abilityAmt(int i, Player p) {
		int ia = 0;
		if (i == 1) {
			ia = getConfig().getInt(p.getName() + ".jump");
		}
		if (i == 2) {
			ia = getConfig().getInt(p.getName() + ".shock");
		}
		if (i == 3) {
			ia = getConfig().getInt(p.getName() + ".insta");
		}
		if (i == 4) {
			ia = getConfig().getInt(p.getName() + ".double");
		}
		if (i == 5) {
			ia = getConfig().getInt(p.getName() + ".final");
		}
		if (i == 6) {
			ia = getConfig().getInt(p.getName() + ".fire");
		}
		if (i == 7) {
			ia = getConfig().getInt(p.getName() + ".poison");
		}
		return ia;
	}

	public String abilityStr(int i) {
		String s = "";
		if (i == 1) {
			s = "Jump Pad";
		} else if (i == 2) {
			s = "Shock Absorber";
		} else if (i == 3) {
			s = "InstaKill";
		} else if (i == 4) {
			s = "Double Jump";
		} else if (i == 5) {
			s = "Final Stand";
		} else if (i == 6) {
			s = "Fire Touch";
		} else if (i == 7) {
			s = "Poison Touch";
		} else {
			s = "Empty";
		}
		return s;
	}

	public int abilityNum(Player p) {
		int i = getConfig().getInt(p.getName() + ".currentAbility");
		return i;
	}

	public void removeOne(int i, Player p) {
		String ab = "";
		if (i == 1) {
			ab = ".jump";
		}
		if (i == 2) {
			ab = ".shock";
		}
		if (i == 3) {
			ab = ".insta";
		}
		if (i == 4) {
			ab = ".double";
		}
		if (i == 5) {
			ab = ".final";
		}
		if (i == 6) {
			ab = ".fire";
		}
		if (i == 7) {
			ab = ".poison";
		}
		int a = getConfig().getInt(p.getName() + ab);
		getConfig().set(p.getName() + ab, a - 1);
	}

	public void configger(Player p) {
		FileConfiguration c = getConfig();
		String n = p.getName();
		if (c.contains(n + ".money") == false) {
			c.set(n + ".money", 0);
		}
		if (c.contains(n + ".jump") == false) {
			c.set(n + ".jump", 0);
		}
		if (c.contains(n + ".shock") == false) {
			c.set(n + ".shock", 0);
		}
		if (c.contains(n + ".insta") == false) {
			c.set(n + ".insta", 0);
		}
		if (c.contains(n + ".double") == false) {
			c.set(n + ".double", 0);
		}
		if (c.contains(n + ".final") == false) {
			c.set(n + ".final", 0);
		}
		if (c.contains(n + ".fire") == false) {
			c.set(n + ".fire", 0);
		}
		if (c.contains(n + ".poison") == false) {
			c.set(n + ".poison", 0);
		}
		if (c.contains(n + ".clear") == false) {
			c.set(n + ".clear", 0);
		}
		if (c.contains(n + ".spam") == false) {
			c.set(n + ".spam", false);
		}
		if (c.contains(n + ".currentAbility") == false) {
			c.set(n + ".currentAbility", 0);
		}
	}

	public ChatColor colourListener(String s) {
		ChatColor cc = ChatColor.WHITE;
		if (s.equalsIgnoreCase("red")) {
			cc = ChatColor.RED;
		}
		if (s.equalsIgnoreCase("black")) {
			cc = ChatColor.BLACK;
		}
		if (s.equalsIgnoreCase("aqua")) {
			cc = ChatColor.AQUA;
		}
		if (s.equalsIgnoreCase("blue")) {
			cc = ChatColor.BLUE;
		}
		if (s.equalsIgnoreCase("darkaqua")) {
			cc = ChatColor.DARK_AQUA;
		}
		if (s.equalsIgnoreCase("darkblue")) {
			cc = ChatColor.DARK_BLUE;
		}
		if (s.equalsIgnoreCase("darkgrey")) {
			cc = ChatColor.DARK_GRAY;
		}
		if (s.equalsIgnoreCase("darkgreen")) {
			cc = ChatColor.DARK_GREEN;
		}
		if (s.equalsIgnoreCase("darkpurple")) {
			cc = ChatColor.DARK_PURPLE;
		}
		if (s.equalsIgnoreCase("darkred")) {
			cc = ChatColor.DARK_RED;
		}
		if (s.equalsIgnoreCase("gold")) {
			cc = ChatColor.GOLD;
		}
		if (s.equalsIgnoreCase("gray")) {
			cc = ChatColor.GRAY;
		}
		if (s.equalsIgnoreCase("green")) {
			cc = ChatColor.GREEN;
		}
		if (s.equalsIgnoreCase("lightpurple")) {
			cc = ChatColor.LIGHT_PURPLE;
		}
		if (s.equalsIgnoreCase("white")) {
			cc = ChatColor.WHITE;
		}
		if (s.equalsIgnoreCase("yellow")) {
			cc = ChatColor.YELLOW;
		}
		return cc;
	}
}