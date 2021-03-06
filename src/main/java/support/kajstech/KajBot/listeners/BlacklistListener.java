package support.kajstech.KajBot.listeners;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import support.kajstech.KajBot.command.PermitCommand;
import support.kajstech.KajBot.utils.IKajBot;
import support.kajstech.KajBot.utils.Info;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BlacklistListener extends ListenerAdapter {

    private static final String URL_REGEX = "(?i)\\b(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?\\b";

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor() == event.getJDA().getSelfUser()) return;


        if (!(PermitCommand.permitted.contains(event.getMessage().getMember()) || event.isFromType(ChannelType.PRIVATE) || IKajBot.isAdmin(event.getMember()) || event.getMember().getRoles().stream().anyMatch(r -> r.getName().equals(Info.BLACKLIST_BYPASS_ID)))) {

            Pattern p = Pattern.compile(URL_REGEX);
            Matcher m = p.matcher(event.getMessage().getContentRaw());
            if (m.find()) {
                if (Info.BLACKLIST_LINKS_ENABLED.equalsIgnoreCase("false")) return;
                IKajBot.sendMessage(event.getMember().getAsMention() + " \u26D4 Hey, beskeder der indeholder links er ikke tilladte herinde", event.getTextChannel());
                event.getMessage().delete().queue();
                return;
            }


            for (String item : Info.BLACKLISTED.split("\\s*,\\s*")) {
                if (Info.BLACKLIST_ENABLED.equalsIgnoreCase("false")) return;
                if (event.getMessage().getContentRaw().contains(item) && Info.BLACKLISTED.length() > 2) {
                    IKajBot.sendMessage(event.getMember().getAsMention() + " \u26D4 Hey, din besked indholde et eller flere ord/links om ikke er tilladte herinde", event.getTextChannel());
                    event.getMessage().delete().queue();
                    return;
                }
            }
        }
    }


}
