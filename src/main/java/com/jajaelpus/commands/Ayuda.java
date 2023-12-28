package com.jajaelpus.commands;

import ca.tristan.jdacommands.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ayuda implements ICommand {

    String command = "";
    public Ayuda(String a) {
        command = a;
    }


    @Override
    public String getName() {
        return command;
    }

    @Override
    public void execute(MessageReceivedEvent messageReceivedEvent) {

            String mensaje = "Para iniciar tu aventura usa el comando\n" +
                    "r!new <Nombre de tu personaje>\n" +
                    "Recuerda que solo puedes usar una palabra para el nombre" +
                    "\n\n" +
                    "Zonas\n" +
                    "" +
                    "\n" +
                    "Las zonas son las siguientes...";

            messageReceivedEvent.getChannel().sendMessage(mensaje).queue();

    }
}
