

package com.laytonsmith.abstraction.bukkit;

import com.laytonsmith.abstraction.AbstractionObject;
import com.laytonsmith.abstraction.MCPlugin;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author layton
 */
public class BukkitMCPlugin implements MCPlugin {

    Plugin p;
    public BukkitMCPlugin(Plugin plugin) {
        this.p = plugin;
    }
    
    public BukkitMCPlugin(AbstractionObject a){
        this((Plugin)null);
        if(a instanceof MCPlugin){
            this.p = ((Plugin)a.getHandle());
        } else {
            throw new ClassCastException();
        }
    }
    
    public Object getHandle(){
        return p;
    }

    public boolean isEnabled() {
        return p.isEnabled();
    }
    
    public boolean isInstanceOf(Class c) {
        if (c.isInstance(p)) {
            return true;
        }
        
        return false;
    }
    
    public Plugin getPlugin() {
        return p;
    }
    
}
