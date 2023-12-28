package com.jajaelpus;


import ca.tristan.jdacommands.JDACommands;
import com.jajaelpus.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Index extends ListenerAdapter {
    public static void main(String[] args) {


        JDACommands jdaCommands = new JDACommands("r!");
            jdaCommands.registerCommand(new New());
            jdaCommands.registerCommand(new Explorar());
            jdaCommands.registerCommand(new Ayuda("ayuda"));
            jdaCommands.registerCommand(new Ayuda("help"));
            jdaCommands.registerCommand(new Ayuda("h"));
            jdaCommands.registerCommand(new Energia());
            jdaCommands.registerCommand(new Mochila());

        JDA jda = JDABuilder.createDefault("MTA4MjE2NTQ4MDk1OTI2NjgyNg.G2KHOm.G68eJ9T4cY9UE48AH1jdcdjdvVAHwJZlL_47ws")
                .setActivity(Activity.playing("un RPG"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(jdaCommands)
                .build();


        giveEnergy();


    }

    private static void giveEnergy() {

        Connection conect = null;
        String adm = "admin";
        String pss = "";
        try {

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            conect = DriverManager.getConnection("jdbc:ucanaccess://E:\\rpg2.accdb", adm, pss);
            Statement stment = conect.createStatement();
            stment.executeUpdate("UPDATE Usuarios SET Usuarios.Energia = Energia+Usuarios.Nivel*1.2\n" +
                    "WHERE (((Usuarios.Energia)<10+([Nivel]-1)*5));");
            stment.executeUpdate("UPDATE usuarios SET usuarios.Energia = Energia-(Energia%1)");
        } catch (Exception e) {

        }
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
        }
        giveEnergy();

    }


}