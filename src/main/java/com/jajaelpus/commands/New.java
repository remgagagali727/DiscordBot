package com.jajaelpus.commands;

import ca.tristan.jdacommands.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class New implements ICommand {


    @Override
    public String getName() {
        return "new";
    }


    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent) {


        Connection conect = null;
        String adm = "admin";
        String pss = "";
        boolean hola = false;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            conect = DriverManager.getConnection("jdbc:ucanaccess://E:\\rpg2.accdb", adm, pss);
            Statement stment = conect.createStatement();
            ResultSet rs = stment.executeQuery("SELECT ID\n FROM USUARIOS\n WHERE ID = " + messageReceivedEvent.getMember().getId());
            hola = rs.next();

            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        if (hola) {
            messageReceivedEvent.getChannel().sendMessage("Ya haz creado un jugador").queue();
        } else {
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                conect = DriverManager.getConnection("jdbc:ucanaccess://E:\\rpg2.accdb", adm, pss);
                Statement stment = conect.createStatement();
                String qry = "INSERT INTO Usuarios\nVALUES ('" + messageReceivedEvent.getMember().getId() + "','" + messageReceivedEvent.getMessage().getContentRaw().split(" ")[1] + "','1','false','0_1','0','10');";
                stment.executeUpdate(qry);

                messageReceivedEvent.getChannel().sendMessage("Jugador registrado con exito").queue();
            } catch (Exception e) {
                System.out.println(e);

            }

        }
    }
}
