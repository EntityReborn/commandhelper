/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.aliasengine.functions;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.junit.runner.RunWith;
import com.laytonsmith.testing.StaticTest;
import com.laytonsmith.puls3.abstraction.MCLocation;
import com.laytonsmith.puls3.abstraction.bukkit.BukkitMCWorld;
import com.laytonsmith.puls3.abstraction.blocks.MCBlock;
import com.laytonsmith.puls3.abstraction.MCWorld;
import com.laytonsmith.puls3.abstraction.MCCommandSender;
import com.laytonsmith.puls3.abstraction.MCConsoleCommandSender;
import com.laytonsmith.puls3.abstraction.MCPlayer;
import com.laytonsmith.puls3.abstraction.MCServer;
import com.laytonsmith.puls3.abstraction.StaticLayer;
import com.laytonsmith.puls3.core.MScriptComplete;
import com.laytonsmith.puls3.core.exceptions.ConfigCompileException;
import com.laytonsmith.puls3.Puls3Plugin;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static com.laytonsmith.testing.StaticTest.*;
import static org.junit.Assert.*;

/**
 *
 * @author Layton
 */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest( { StaticLayer.class })

public class PlayerManangementTest {

    MCServer fakeServer;
    MCPlayer fakePlayer;
    
    public PlayerManangementTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        mockStatic(StaticLayer.class);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        fakeServer = GetFakeServer();
        fakePlayer = GetOp("wraithguard01", fakeServer);
        when(fakePlayer.getServer()).thenReturn(fakeServer);
        Puls3Plugin.myServer = fakeServer;
        String name = fakePlayer.getName();
        when(fakeServer.getPlayer(name)).thenReturn(fakePlayer);
    }
    
    @After
    public void tearDown() {
    }

    @Test public void testPlayer() throws ConfigCompileException{
        String script = "player()";
        assertEquals(fakePlayer.getName(), SRun(script, fakePlayer));
        assertEquals("null", SRun(script, null));
    }
    
    @Test public void testPlayer2() throws ConfigCompileException{
        String script = "msg(player())";
        MCConsoleCommandSender c = GetFakeConsoleCommandSender();
        Run(script, c);
        verify(c).sendMessage("~console");
    }
    
    @Test 
    public void testPlayer3() throws ConfigCompileException{
        MCCommandSender c = GetFakeConsoleCommandSender();
        assertEquals("~console", SRun("player()", c));
    }
    
    @Test public void testAllPlayers() throws ConfigCompileException{
        String script = "all_players()";
        String done = SRun(script, fakePlayer);
        //This output is too long to test with msg()        
        assertEquals("{wraithguard01, wraithguard02, wraithguard03}", done);
    }
    
    @Test public void testPloc() throws ConfigCompileException{
        String script = "ploc()";
        BukkitMCWorld w = GetWorld("world");
        MCLocation loc = StaticLayer.GetLocation(w, 0, 1, 0);
        when(fakePlayer.getLocation()).thenReturn(loc);
        when(fakePlayer.getWorld()).thenReturn(w);
        final StringBuilder done = new StringBuilder();
        Run(script, fakePlayer, new MScriptComplete() {

            public void done(String output) {
                done.append(output);
            }
        });
        assertEquals("{0.0, 0.0, 0.0, world}", done.toString());
    }
    
    /*@Test*/ public void testSetPloc() throws ConfigCompileException{
        MCWorld w = GetWorld("world");
        Puls3Plugin.myServer = fakeServer;
        String name = fakePlayer.getName();
        when(fakeServer.getPlayer(name)).thenReturn(fakePlayer);
        when(fakePlayer.getWorld()).thenReturn(w);
        MCLocation loc = StaticTest.GetFakeLocation(w, 0, 0, 0);
        when(fakePlayer.getLocation()).thenReturn(loc);
        
        Run("set_ploc(1, 1, 1)", fakePlayer);
        //when(StaticLayer.GetLocation(w, 1, 2, 1)).thenReturn(loc);
        MCLocation loc1 = StaticTest.GetFakeLocation(w, 1, 2, 1);
        assertEquals(fakePlayer.getLocation().getX(), loc1.getX(), 0.00000000000001);//verify(fakePlayer).teleport(loc1);
        
        Run("set_ploc(array(2, 2, 2))", fakePlayer);
        verify(fakePlayer).teleport(StaticLayer.GetLocation(w, 2, 3, 2, 0, 0));
        
        Run("set_ploc('" + fakePlayer.getName() + "', 3, 3, 3)", fakePlayer);
        verify(fakePlayer).teleport(StaticLayer.GetLocation(w, 3, 4, 3, 0, 0));
        
        Run("set_ploc('" + fakePlayer.getName() + "', array(4, 4, 4))", fakePlayer);
        verify(fakePlayer).teleport(StaticLayer.GetLocation(w, 4, 5, 4, 0, 0));
    }
    
    @Test public void testPcursor() throws ConfigCompileException{  
        MCBlock b = mock(MCBlock.class);
        Puls3Plugin.myServer = fakeServer;
        when(fakeServer.getPlayer(fakePlayer.getName())).thenReturn(fakePlayer);
        when(fakePlayer.getTargetBlock(null, 200)).thenReturn(b);
        MCWorld w = mock(MCWorld.class);
        when(b.getWorld()).thenReturn(w);
        Run("pcursor()", fakePlayer);
        Run("pcursor('" + fakePlayer.getName() + "')", fakePlayer);
        verify(fakePlayer, times(2)).getTargetBlock(null, 200);
    }
    
    @Test public void testKill() throws ConfigCompileException{        
        Run("kill()", fakePlayer);
        Run("kill('" + fakePlayer.getName() + "')", fakePlayer);
        verify(fakePlayer, times(2)).setHealth(0);
    }
    
    //@Test
    public void testPgroup() throws ConfigCompileException{
        Run("", fakePlayer);
        Run("", fakePlayer);
    }
    
//    //@Test
//    public void testPinfo(){
//        
//    }
//    
//    //@Test
//    public void testPworld(){
//        
//    }
//    
//    //@Test
//    public void testKick(){
//        
//    }
//    
//    //@Test
//    public void testSetDisplayName(){
//        
//    }
//    
//    //@Test
//    public void testResetDisplayName(){
//        
//    }
//    
//    //@Test
//    public void testPFacing(){
//        
//    }
//    
//    //@Test
//    public void testPinv(){
//        
//    }
//    
//    //@Test
//    public void testSetPinv(){
//        
//    }
}
