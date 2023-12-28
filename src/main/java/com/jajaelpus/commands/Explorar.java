package com.jajaelpus.commands;

import ca.tristan.jdacommands.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.hsqldb.lib.HsqlTaskQueue;

import java.sql.*;
import java.util.ArrayList;

public class Explorar implements ICommand {
    @Override
    public String getName() {
        return "explorar";
    }

    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent) {
        boolean convertido = false;
        if (messageReceivedEvent.getMessage().getContentRaw().split(" ").length < 2) {
            messageReceivedEvent.getChannel().sendMessage("No haz elegido una zona <:exclamacion:1118431248134713344>").queue();
            convertido = true;
        }
        boolean posible = false;

        int zona = Integer.parseInt(messageReceivedEvent.getMessage().getContentRaw().split(" ")[1]);

        String mensaje = "";
        Connection conect = null;
        String adm = "admin";
        String pss = "";
        boolean hola = false;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            conect = DriverManager.getConnection("jdbc:ucanaccess://E:\\rpg2.accdb", adm, pss);
            Statement stment = conect.createStatement();
            ResultSet rs = stment.executeQuery("SELECT energia FROM Usuarios WHERE ID = "+messageReceivedEvent.getMember().getId()+";");
            rs.next();
            posible = rs.getInt(1)>(zona-1);
            if (!posible)
            messageReceivedEvent.getChannel().sendMessage("No tienes energia porfavor intenta mas tarde <:exclamacion:1118431248134713344>").queue();
        } catch (Exception e) {

        }


        if (posible) {
            try {

                convertido = true;
                mensaje = lootGenerator(zona, messageReceivedEvent);
                messageReceivedEvent.getChannel().sendMessage(mensaje).queue();
            } catch (Exception e) {

            }
        }

        if (!convertido && posible) {
            messageReceivedEvent.getChannel().sendMessage("La zona debe colocarse en numeros <:exclamacion:1118431248134713344>").queue();
        }


    }

    public String lootGenerator(int zona, MessageReceivedEvent messageReceivedEvent) {

        String mensaje = "";
        Connection conect = null;
        String adm = "admin";
        String pss = "";
        boolean hola = false;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            conect = DriverManager.getConnection("jdbc:ucanaccess://E:\\rpg2.accdb", adm, pss);
            Statement stment = conect.createStatement();

            int proba = (int) (Math.random() * 1000000);


            ResultSet rs = stment.executeQuery("SELECT * FROM ZONAS WHERE ID = " + zona + ";");
            rs.next();
            String zonaS = rs.getString(2);

            String qry = "SELECT * FROM Items WHERE zona = " + zona + " AND probabilidad > " + proba + " AND tipo = 1;";

            stment.executeUpdate("UPDATE Usuarios SET Usuarios.Energia = Usuarios.Energia - " + zona + " WHERE Usuarios.ID = " + messageReceivedEvent.getMember().getId());

            mensaje += "Gastaste " + zona + " de energia <:energia:1118621303784607924>\n";
            mensaje += "Viajaste a la zona " + zonaS + ".\n";
            mensaje += "Y obtuviste los siguientes objetos:\n";


            ArrayList<String> ntempMochila = new ArrayList<>();
            ArrayList<String[]> nbackpack = new ArrayList<>();
            String items[] = null;

            rs = stment.executeQuery("SELECT BP FROM USUARIOS WHERE ID =" + messageReceivedEvent.getMember().getId());
            while (rs.next()) {
                String tempMochila[] = rs.getString(1).split("-");
                String backpack[][] = new String[tempMochila.length][2];

                for (int i = 0; i < tempMochila.length; i++) {
                    items = tempMochila[i].split("_");
                    backpack[i][0] = items[0];
                    backpack[i][1] = items[1];
                    nbackpack.add(backpack[i]);

                    ntempMochila.add(tempMochila[i]);
                }
            }


            rs = stment.executeQuery(qry);
            while (rs.next()) {
                int itemamount = (int) (Math.random() * 5 * rs.getInt(6) + 1);
                mensaje += " ° " + itemamount + "x " + rs.getString(2) + " " + rs.getString(3) + "\n";
                boolean find = false;
                for (int i = 1; i < nbackpack.size(); i++) {

                    if (nbackpack.get(i)[0].matches(rs.getString(1))) {
                        String[] a = {nbackpack.get(i)[0], String.valueOf(Integer.parseInt(nbackpack.get(i)[1]) + itemamount)};
                        nbackpack.set(i, a);
                        find = true;
                        break;
                    }

                }
                if (!find) {
                    String[] a = {rs.getString(1), String.valueOf(itemamount)};
                    nbackpack.add(a);
                }
            }


            String newBackPack = "";
            for (String[] nbp : nbackpack) {
                newBackPack += nbp[0] + "_" + nbp[1] + "-";
            }

            newBackPack = newBackPack.substring(0, newBackPack.length() - 1);

            stment.executeUpdate("UPDATE Usuarios SET Usuarios.BP = '" + newBackPack + "' WHERE Usuarios.ID = " + messageReceivedEvent.getMember().getId() + ";");

            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return mensaje;

    }
}

