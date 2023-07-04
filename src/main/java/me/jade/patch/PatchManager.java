package me.jade.patch;/*

@Author https://github.com/akita0
2023

*/

import me.jade.patch.classic.AutoDrink;
import me.jade.patch.classic.Speed;
import me.jade.patch.classic.Use;

import java.util.ArrayList;

public class PatchManager {
    public static ArrayList<Patch> patches = new ArrayList<Patch>();
    public static ArrayList<String> notify = new ArrayList<String>();
    public static void registerPatchs() {
            loadPatch(new AutoDrink());
            loadPatch(new Speed());
            loadPatch(new Use());
    }
    public static void loadPatch(Patch p) {
        patches.add(p);
        System.out.println("Patch added : "+  p.getName());
    }
}
