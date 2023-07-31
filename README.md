# TipCord
- Tipcord welcomes everyone to the world of crypto, even those new to the concept! Whether you're a Discord enthusiast or a crypto beginner, this software is designed to make tipping with Solana an enjoyable and user-friendly experience.
- With the adaptable REST-API design, this system can seamlessly integrate into a wide array of platforms, unlocking limitless possibilities for implementation.

🎉 For Discord users not yet familiar with crypto, Tipcord serves as a friendly introduction to the exciting world of Solana. Easily manage your SOL, even if you're new to the game! No complex procedures - just simple commands to get started with a secure wallet and access to the world of tipping.

💫 But it doesn't stop there! Tipcord empowers you to introduce your friends to the wonders of Solana as well. Share the joy of tipping with your friends and let them onboard into the realm of Solana crypto. It's not just about tipping; it's about fostering a supportive community and spreading the crypto cheer!

# 🔒 Safety is our priority. 
- Tipcord ensures that your assets are protected with encrypted private keys and a secure environment for worry-free tipping.

Bot-Invite Link: https://discord.com/oauth2/authorize?permissions=347200&scope=bot+applications.commands&client_id=1134177162270871562 or host it yourself :)

Example transaction: 

![Screenshot (1842)](https://github.com/nauriculus/TipCord/assets/24634581/1ad06f83-bd6b-487e-9981-efc457b9090d)

# Setup
Install all npm packages for the TipCordAPI, change the mysql credentials and SSL keys and run it using npm e.g npm start TipCordAPI.js
For the discord bot you will also need to create a new one here: discord.com/developers/applications and replace the Key inside the TipCord.java class.

INFO: 
When sending tips / withdrawals make sure you have enough SOL to cover transaction fees and don't use the maximum SOL amount as withdrawal amount as some lamports are required for gas. 

# Demo
A demo video can be found here: https://www.youtube.com/watch?v=EOEIXyqYOs0
