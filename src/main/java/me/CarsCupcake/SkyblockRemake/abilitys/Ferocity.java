package me.CarsCupcake.SkyblockRemake.abilitys;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import me.CarsCupcake.SkyblockRemake.Main;
import me.CarsCupcake.SkyblockRemake.Skyblock.Calculator;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.CarsCupcake.SkyblockRemake.Items.SpawnEggEntitys;
import me.CarsCupcake.SkyblockRemake.Skyblock.SkyblockEntity;
import me.CarsCupcake.SkyblockRemake.Skyblock.SkyblockPlayer;
import me.CarsCupcake.SkyblockRemake.Skyblock.SkyblockScoreboard;

public class Ferocity {
	public static void particles(Entity entity) {
	
		final Particle.DustOptions dust = new Particle.DustOptions(
                Color.fromRGB((int) 255, (int) 0, (int) 0), 1);
		Location manage = entity.getLocation();
		
		Vector a = null;
		Vector b = null;
		Location start;
		if(manage.getYaw() >= -45 && manage.getYaw() <= 45) {
			 start = manage.add(-1,0.5,0);
			a = start.toVector();
				 b =  start.add(2, 1.5, 0).toVector();
		}else {
			if(manage.getYaw() >= 135 || manage.getYaw() <= -135) {
				start = manage.add(1,0.5,0);
				a = start.toVector();
					 b =  start.add(-2, 1.5, 0).toVector();
			}else {
				if(manage.getYaw() >= 45 && manage.getYaw() <= 135) {
					start = manage.add(0,0.5,-1);
					a = start.toVector();
						 b =  start.add(0, 1.5, 2).toVector();
			}else {
				start = manage.add(0,0.5,1);
				a = start.toVector();
					 b =  start.add(0, 1.5, -2).toVector();
			}
		}
		}
		
		
				
				 
				Vector between = b.subtract(a); //get the vector between two points
				double length = between.length(); //get the length of the distance
				between.normalize().multiply(0.3); //normalize our vector and specify the spacing
				double steps = (double)((double)length / 0.3); //determine number of steps
				
				for (int i = 0; i < steps; i++) { //iterate up to but not beyond point b
				    Vector point = a.add(between);
				    entity.getLocation().getWorld().spawnParticle(Particle.REDSTONE, point.getX(), point.getY(), point.getZ(),1, dust);
				}
                            

        

	}
	public static void playSound(Location loc) {
		loc.getWorld().playSound(loc, Sound.ITEM_FLINTANDSTEEL_USE, 1f, 0);
		
		loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.1f, 2f);
	}

public static void hit(LivingEntity e, int damage, boolean crit, Player player) {
	if(e == null)
		return;
	particles(e);
	playSound(e.getEyeLocation());
	if(Main.currentityhealth.get(e) == null && !SkyblockEntity.livingEntity.containsKey(e))
		return;


	Calculator calculator = new Calculator(true);
	calculator.damage = damage;
	calculator.isCrit = crit;
	calculator.damageEntity(e, SkyblockPlayer.getSkyblockPlayer(player));
	calculator.showDamageTag(e);


}


public static void playerhit(Player player, int totaldmg, boolean crit, EntityDamageByEntityEvent event) {
	
	playSound(player.getLocation());
	particles(player);
	SkyblockPlayer p = SkyblockPlayer.getSkyblockPlayer(player);
	if(Main.absorbtion.get(player) - totaldmg  < 0) {
		float restdamage =   (float)totaldmg - (float) Main.absorbtion.get(player);
		Main.absorbtion.replace(player, 0);
		p.setHealth( p.currhealth - (int)restdamage);
	}else {
		Main.absorbtion.replace(player, Main.absorbtion.get(player) - totaldmg);
	}
	
	if(p.currhealth <= 0) {
		player.setHealth(0);
		Main.deathPersons.add(player);
	}else
	Main.updatebar(p);
	
	final int FINAL_DAMAGE = totaldmg;
	Location loc = new Location(event.getEntity().getWorld(), event.getEntity().getLocation().getX() ,event.getEntity().getLocation().getY() + 0.5 , event.getEntity().getLocation().getZ());
	
	ArmorStand stand = (ArmorStand) event.getEntity().getWorld().spawn(loc, ArmorStand.class, armorstand ->{
		armorstand.setVisible(false);
	
		armorstand.setGravity(false);
		armorstand.setMarker(true);
		
	  
		armorstand.setCustomNameVisible(true);
	
		armorstand.setInvulnerable(true);
	if(crit) {
		String name = "§f✧";
		String num = "" + (int) FINAL_DAMAGE;
		int col =1;
		int coltype = 1;
		String colstr = "§f";
		
		for (char x : num.toCharArray()) {
			name = name + colstr + x;
			++col;
			if(col ==2) {
				col = 0;
				++coltype;
				switch(coltype) {
				case 1:
					colstr = "§f";
					break;
				case 2:
					colstr = "§e";
					break;
				case 3:
					colstr = "§6";
					coltype = 0;
					break;
					
				}
				
			}
		}
		
		armorstand.setCustomName(name);
	}else
		armorstand.setCustomName("§7" + (int)FINAL_DAMAGE);
	armorstand.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 999999));
	armorstand.addScoreboardTag("damage_tag");
	armorstand.setArms(false);
	
	armorstand.setBasePlate(false);});
	
	Main.getMain().killarmorstand(stand);
	
	
	
}




}
