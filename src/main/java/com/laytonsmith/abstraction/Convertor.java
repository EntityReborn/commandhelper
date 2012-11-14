
package com.laytonsmith.abstraction;

import com.laytonsmith.commandhelper.CommandHelperPlugin;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * This should be implemented once for each server type. It mostly wraps
 * static methods, but also provides methods for getting other server specific
 * things. You can get an instance of the current Convertor by looking for the
 * <code>@convert</code> tag. StaticLayer wraps all the functionality for you
 * however.
 * @author layton
 */
public interface Convertor {

    public MCLocation GetLocation(MCWorld w, double x, double y, double z, float yaw, float pitch);
    public Class GetServerEventMixin();

    public MCEnchantment[] GetEnchantmentValues();

	/**
	 * Returns the enchantment, given an enchantment name (or a string'd number).
	 * Returns null if no such enchantment exists.
	 * @param name
	 * @return 
	 */
    public MCEnchantment GetEnchantmentByName(String name);

    public MCServer GetServer();

    public MCItemStack GetItemStack(int type, int qty);

    public void Startup(CommandHelperPlugin chp);
    
    public int LookupItemId(String materialName);

    public String LookupMaterialName(int id);

    public MCItemStack GetItemStack(int type, int data, int qty);
    
    /**
     * A future runnable is run on a server accessible thread at roughly the time specified in the future.
     * This is no guarantee however, as the particular server implementation may make this hard to do. The
     * value returned is 
     * @param r
     * @return 
     */
    public int SetFutureRunnable(long ms, Runnable r);

    public void ClearAllRunnables();

    public void ClearFutureRunnable(int id);

    public int SetFutureRepeater(long ms, long initialDelay, Runnable r);

    public MCEntity GetCorrectEntity(MCEntity e);
	
	/**
	 * Gets the inventory of the specified entity, or null if the entity id
	 * is invalid
	 * @param entityID
	 * @return 
	 */
	public MCInventory GetEntityInventory(int entityID);
	
	/**
	 * Returns the inventory of the block at the specified location, if it is
	 * an inventory type block, or null if otherwise invalid.
	 * @param location
	 * @return 
	 */
	public MCInventory GetLocationInventory(MCLocation location);
	
	/**
	 * Run whenever the server is shutting down (or restarting). There is no
	 * guarantee provided as to what thread the runnables actually run on, so you should
	 * ensure that the runnable executes it's actions on the appropriate thread
	 * yourself.
	 * @param r 
	 */
	public void addShutdownHook(Runnable r);
	
	/**
	 * Runs all the registered shutdown hooks. This should only be called by the shutdown mechanism.
	 * After running, each Runnable will be removed from the queue.
	 */
	public void runShutdownHooks();
	
	/**
	 * Runs some task on the "main" thread, possibly now, possibly in the future, and
	 * possibly even on this thread. However, if the platform needs some critical action
	 * to happen on one thread, (for instance, UI updates on the UI thread) those actions
	 * will occur here.
	 * @param r 
	 */
	public void runOnMainThreadLater(Runnable r);
	
	/**
	 * Works like runOnMainThreadLater, but waits for the task to finish.
	 * @param r 
	 */
	public <T> T runOnMainThreadAndWait(Callable<T> callable) throws Exception;
    
}
