package com.nauriculus.tipcord.listener;

import java.awt.Color;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nauriculus.tipcord.TipCordAdapter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class EventListener extends ListenerAdapter {
	
	
	    @Override
	    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if(event.getInteraction() == null) {
			return;
		}
		
		if (event.getName().equalsIgnoreCase("create")) {
        	ExecutorService executorService = Executors.newSingleThreadExecutor();			
        	executorService.execute(new Runnable() {
			  public void run() {
			 
				event.deferReply(true).queue(); // Let the user know we received the command before doing anything else
				InteractionHook hook = event.getHook(); // This is a special webhook that allows you to send messages without having permissions in the channel and also allows ephemeral messages
				hook.setEphemeral(true);
					
				OptionMapping pin = event.getOption("pin");
				Integer pinPassword = pin.getAsInt();
				 
				if(TipCordAdapter.checkIfWalletExists(event.getUser().getId())) {
			    	hook.sendMessage("You already have a TipCord wallet. Use /wallet to check your TipCord wallet.").setEphemeral(true).queue();
			    	return;
				}
				
				if(!TipCordAdapter.checkIfWalletExists(event.getUser().getId())) {
					String wallet = TipCordAdapter.getUserWallet("" + pinPassword, event.getUser().getId());
					EmbedBuilder builder = new EmbedBuilder();
					builder.setTitle("WALLET CREATED");
					builder.setDescription(":white_check_mark: Great news! Your new TipCord wallet was just created!");
				
					builder.addField(":warning: Reminder", "Your secret pin is: ``" + pinPassword +"`` Make sure to remember this pin, otherwise your funds will be lost.", false);
					builder.addField("Publickey", "``" + wallet + "``", false);
					builder.setColor(Color.green);
			
					builder.setFooter("Powered by TipCord");
					
					hook.sendMessageEmbeds(builder.build()).queue();
					return;
			    	
				}
			  }
			});
			executorService.shutdown();
         }
	
		 if (event.getName().equalsIgnoreCase("wallet")) {
	        	ExecutorService executorService = Executors.newSingleThreadExecutor();			
	        	executorService.execute(new Runnable() {
				  public void run() {
				 
					event.deferReply(true).queue(); // Let the user know we received the command before doing anything else
					InteractionHook hook = event.getHook(); // This is a special webhook that allows you to send messages without having permissions in the channel and also allows ephemeral messages
					hook.setEphemeral(true);
						
					if(!TipCordAdapter.checkIfWalletExists(event.getUser().getId())) {
						hook.sendMessage("You don't have any wallet yet! Please use /create first.").queue();
						return;
					}
					
					if(TipCordAdapter.checkIfWalletExists(event.getUser().getId())) {
						String wallet = TipCordAdapter.getWalletWithoutPIN(event.getUser().getId());
				    	hook.sendMessage("Your TipCord wallet is: " + wallet).setEphemeral(true).queue();
					}
				  }
				});
				executorService.shutdown();
	        }
	    
		 if (event.getName().equalsIgnoreCase("tip")) {
	  	      
	        	ExecutorService executorService = Executors.newSingleThreadExecutor();
				executorService.execute(new Runnable() {
				public void run() {
				    event.deferReply(true).queue(); // Let the user know we received the command before doing anything else
				    InteractionHook hook = event.getHook(); // This is a special webhook that allows you to send messages without having permissions in the channel and also allows ephemeral messages
				    hook.setEphemeral(true);
				 
				
				    OptionMapping receiver = event.getOption("receiver");
			            OptionMapping amountS = event.getOption("amount");
			        
			            OptionMapping pin = event.getOption("pin");
				    Integer pinPassword = pin.getAsInt();
			          
			            String s = amountS.getAsString();
			         
			            User receiverUser = receiver.getAsUser();

					
					double convertedAmount;
					try {
					  convertedAmount = Double.parseDouble(s);
					  
					}
					catch(NumberFormatException e) {
						hook.sendMessage("Your amount is invaild! Example amount: ``0.01``").queue();
						return;
					}
					
					if(convertedAmount == 0.0 || convertedAmount == 0 || convertedAmount < 0.0 || convertedAmount < 0) {
						hook.sendMessage("Your amount is invaild! Example amount: ``0.01``").queue();
						return;
					}
					
					String amountString = "" + convertedAmount;

					if(!TipCordAdapter.checkIfWalletExists(event.getUser().getId())) {
						hook.sendMessage("You don't have any wallet yet! Please use /create first.").queue();
						return;
					}
					
					if(!TipCordAdapter.checkIfWalletExists(receiverUser.getId())) {
						hook.sendMessage("This user has no TipCord wallet yet.").queue();
						return;
					}
					
					if(TipCordAdapter.checkIfWalletExists(receiverUser.getId())) {
						
						String wallet = TipCordAdapter.getUserWallet("" + pinPassword, receiverUser.getId());
						
						String signature = TipCordAdapter.sendTransaction("" + pinPassword, event.getUser().getId(), wallet, amountString);
						
						if(!signature.equalsIgnoreCase("ERROR")) {
							EmbedBuilder builder = new EmbedBuilder();
							builder.setTitle("TRANSACTION SENT");
							builder.setDescription(":white_check_mark: Your transaction is pending now, but should be confirmed soon.");
							builder.setColor(Color.green);
							builder.setFooter("Powered by TipCord");
							
							hook.sendMessageEmbeds(builder.build()).addActionRow(Button.link("https://solscan.io/tx/" + signature, "Transaction")).queue();
							return;
						}
						else {
							hook.sendMessage("Your transaction failed. Please try again!").queue();
							return;
							}		  
						}
				}
				});
				executorService.shutdown();

	        }
	             
	        if (event.getName().equalsIgnoreCase("withdraw")) {
	  	      
	        	ExecutorService executorService = Executors.newSingleThreadExecutor();
				executorService.execute(new Runnable() {
				public void run() {
				
					event.deferReply(true).queue(); // Let the user know we received the command before doing anything else
					InteractionHook hook = event.getHook(); // This is a special webhook that allows you to send messages without having permissions in the channel and also allows ephemeral messages
					hook.setEphemeral(true);
				 
				
				    OptionMapping wallet = event.getOption("wallet");
			        OptionMapping amountS = event.getOption("amount");
			        OptionMapping pin = event.getOption("pin");
					Integer pinPassword = pin.getAsInt();
			          
			        String s = amountS.getAsString();
			         
			        String receiver = wallet.getAsString();

					
					double convertedAmount;
					try
					{
					  convertedAmount = Double.parseDouble(s);
					  
					}
					catch(NumberFormatException e)
					{
						hook.sendMessage("Your amount is invaild! Example amount: ``0.01``").queue();
						return;
					}
					
					if(convertedAmount == 0.0 || convertedAmount == 0 || convertedAmount < 0.0 || convertedAmount < 0) {
						hook.sendMessage("Your amount is invaild! Example amount: ``0.01``").queue();
						return;
					}
					
					String amountString = "" + convertedAmount;
					String signature = TipCordAdapter.sendTransaction("" + pinPassword, event.getUser().getId(), receiver, amountString);
					if(!signature.equalsIgnoreCase("ERROR")) {
						EmbedBuilder builder = new EmbedBuilder();
						builder.setTitle("TRANSACTION SENT");
						builder.setDescription(":white_check_mark: Your transaction is pending now, but should be confirmed soon.");
					
						builder.setColor(Color.green);
				
						builder.setFooter("Powered by TipCord");
						
						hook.sendMessageEmbeds(builder.build()).addActionRow(Button.link("https://solscan.io/tx/" + signature, "Transaction")).queue();
						
						return;
					}
					else {
						hook.sendMessage("Your transaction failed. Please try again!").queue();
						return;
					}		  
				}
				});
				executorService.shutdown();
	        }
		}
}
