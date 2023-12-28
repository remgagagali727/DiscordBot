package com.jajaelpus.commands;

import ca.tristan.jdacommands.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Energia implements ICommand {
    @Override
    public String getName() {
        return "energia";
    }

    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent) {
        Connection conect = null;
        String adm = "admin";
        String pss = "";
        try {

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            conect = DriverManager.getConnection("jdbc:ucanaccess://E:\\rpg2.accdb", adm, pss);
            Statement stment = conect.createStatement();
            ResultSet rs = stment.executeQuery("SELECT Energia, Nivel FROM USUARIOS WHERE ID = "+messageReceivedEvent.getMember().getId());
            rs.next();
            String mensaje = "Actualmente tienes "+rs.getString(1)+" de energia de un maximo de "+(10+(rs.getInt(2)-1)*5)+"  <:energia:1118621303784607924>";
            messageReceivedEvent.getChannel().sendMessage(mensaje).queue();
        } catch (Exception e) {

        }
    }
}
