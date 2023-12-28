package com.jajaelpus.commands;

import ca.tristan.jdacommands.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Mochila implements ICommand {
    @Override
    public String getName() {
        return "mochila";
    }

    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent) {

        String mensaje = "";
        Connection conect = null;
        String adm = "admin";
        String pss = "";
        boolean hola = false;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            conect = DriverManager.getConnection("jdbc:ucanaccess://E:\\rpg2.accdb", adm, pss);
            Statement stment = conect.createStatement();

            int proba = (int) (Math.random()*1000000);


            ResultSet rs = stment.executeQuery("SELECT BP FROM Usuarios WHERE ID = "+messageReceivedEvent.getMember().getId());

            String items[] = null;

            rs.next();
            String mochila = "Tienes los siguientes objetos en el inventario:\n";

                String tempMochila[] = rs.getString(1).split("-");
                String backpack[][] = new String[tempMochila.length][2];

                for (int i = 0;i < tempMochila.length;i++) {
                    items = tempMochila[i].split("_");
                    backpack[i][0] = items[0];
                    backpack[i][1] = items[1];
                    ResultSet newrs = stment.executeQuery("SELECT Nombre, EmogiID FROM Items WHERE ID = " + backpack[i][0] + ";");
                    newrs.next();
                    if (i != 0)
                        mochila += "Tienes " + items[1] + " " + newrs.getString(1) + " " + newrs.getString(2) + "\n";
                }


            messageReceivedEvent.getChannel().sendMessage(mochila).queue();

            rs.close();
        } catch (Exception e) {
            System.out.println("No tienes ningun objeto en tu mochila");
        }

    }
}
