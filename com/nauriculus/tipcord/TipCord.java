package com.nauriculus.tipcord;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.nauriculus.tipcord.listener.EventListener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class TipCord {

	public static JDABuilder builder;
	public static JDA jda;

	public static void main(String[] args) throws InterruptedException {
		// The JDABuilder is used to create a new instance of a JDA bot with the given API key
		builder = JDABuilder.createDefault("KEY");

		// Sets the status of the bot to ONLINE
		builder.setStatus(OnlineStatus.ONLINE);

		// Adds an instance of the EventListener class to handle events triggered by the bot
		builder.addEventListeners(new EventListener());

		try {
		    // Builds the JDA instance
		    jda = builder.build();
			
		    try {
		      Thread.sleep(10000);
		    } catch (InterruptedException e3) {
		      System.out.println(e3.getMessage());
		    }
			
		    // Creates a list of CommandData objects to define the available slash commands for the bot
		    List<CommandData> commandData = new ArrayList<>();
			
			// Adds a "wallet" command to check the user's Tipcord wallet
		    commandData.add(Commands.slash("wallet", "Check your Tipcord wallet"));
		    
		    // Adds a "create" command to create a new Tipcord wallet with an optional password PIN
		    OptionData pinOption = new OptionData(OptionType.INTEGER, "pin", "The password PIN to decrypt your wallet", true);
		    commandData.add(Commands.slash("create", "Create a new Tipcord wallet").addOptions(pinOption));		
		    
		    // Adds a "tip" command to send a tip from the user's Tipcord wallet to another user's wallet with an optional password PIN
		    OptionData option4 = new OptionData(OptionType.USER, "receiver", "The user who receives the tip", true);
		    OptionData pinOption2 = new OptionData(OptionType.INTEGER, "pin", "The password PIN to encrypt your wallet", true);
		    OptionData option5 = new OptionData(OptionType.STRING, "amount", "The amount in SOL you want to tip", true);
		    commandData.add(Commands.slash("tip", "Tip someone using your Tipcord wallet").addOptions(option4, option5, pinOption2));
		    
		    // Adds a "withdraw" command to withdraw SOL from the user's Tipcord wallet to another wallet with an optional password PIN
		    OptionData option7 = new OptionData(OptionType.STRING, "wallet", "The wallet that receives the SOL", true);
		    OptionData option8 = new OptionData(OptionType.STRING, "amount", "The amount in SOL you want to withdraw", true);
		    OptionData secretPinOption = new OptionData(OptionType.INTEGER, "pin", "The password PIN to encrypt your wallet", true);
		    commandData.add(Commands.slash("withdraw", "Withdraw SOL from your Tipcord wallet to any other").addOptions(option7, option8, secretPinOption));
		     
		    // Queues the creation of the defined slash commands for the bot
		    jda.updateCommands().addCommands(commandData).queue();
		    
		} catch (LoginException e) {
			// Prints the stack trace of the caught LoginException
			e.printStackTrace();
		}
	}

}
