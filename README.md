# RedUtils --  a utility plugin that helps minecraft (digital) redstoners debug, profile and refine their designs!

(The plugin is coded in a rush, the code is a total mess. It works though :))

After designing a large circuit, are you:

* struggling to measure its run time using stop watches and great intuition?
* constantly running between the inputs and outputs, just to pull some levers and check the output?
* using your calculator while inputting numbers and converting the result to decimal?
* taking screenshots just to capture short pulses causing a glitch in your design?

If so, then the plugin is for you!

## Compatible Versions

I am using Paper 1.14.3, other versions of Spigot or Bukkit Server variants should be OK too.

## Getting started

The plugin functions by providing a "wand" and some useful commands.

The core command is `/redutils`, or `/ru` for short.

First, use `/ru wand` to acquire the wand, which is basically just a red dye (Getting it from creative tabs is fine too).

![2019-07-14_13.00.37](README.assets\2019-07-14_13.00.37.png)

## Inputs

RedUtils helps you easily manage different inputs in your circuit so that you don't have to fly around and toggle infinitely many levers. 

![2019-07-14_13.16.42](README.assets\2019-07-14_13.16.42.png)

In this example, we will use a 32-bit full-adder to demonstrate the usage of the plugin.

### Creating input configuration

We first create a input configuration named `input1` by typing:

```
/ru in new input1 slice
```

We are now in "slice" mode. This allows us to select all the inputs (levers) in a 1\*1\*n slice. 

Holding the wand, we first right click the most-significant bit of our input (which is the left most lever in the pic)(be sure to click the lever, not the block beneath!).

Then, we right click the least-significant bit of out input.

If everything is fine, green particles will appear on every input lever in this slice.

![2019-07-14_13.27.11](README.assets\2019-07-14_13.27.11.png)

(Some farther particles are not visible)

Now you have created an input configuration named "input", to check, type `/ru in list`

![1563082153235](README.assets\1563082153235.png)

Yes, that's it!

### Setting inputs

Then it's time to input some data into our circuit. 

Start simple, type `\ru in set input1 bin 1`

![1563082606319](README.assets\1563082606319.png)

This sets the input of "input1" to 1. 

**Note:** Due to technical reasons, currently an input lever must be directly connected to a redstone. otherwise though you can see the lever flip, the redstone won't be powered unless another block update is given!

![1563082520363](README.assets\1563082520363.png)

(This is OK)

![1563082562170](README.assets\1563082562170.png)

(This is not OK, sadly)

It is fine to use `_` as a separator if your input is long:

`/ru in set bin 1011_1101`

It is also fine to use negative numbers:

`/ru in set bin -1001`

![1563082763752](README.assets\1563082763752.png)

You can use decimals, so no more calculator conversions:

`/ru in set dec 23333`

![1563082835515](README.assets\1563082835515.png)

There are 6 inputs modes, namely:

1. `bin` mode: raw binary,  0s will be padded on the left
2. `binPadRight` mode: raw binary, 0s will be padded on the right (useful if you are inputting fractions)
3. `dec` mode: decimal, 0s will be padded on the left
4. `decFrac` mode: pure decimal fraction (E.g 0.114514), 0s will be padded on the right
5. `hex` mode: hexadecimal, 0s will be padded on the left. (Note: You can use this for BCD input!)
6. `hexPadRight` mode: ...

Additionally, you can use `/ru in 0s input1`, or `/ru in 1s input1` to fill the input with 0s or 1s!

Very useful, isn't it?

### Deleting inputs

Deleting input can be done using `/ru in del input1`.

Or the input configuration will be automatically deleted if the plugin detects that a lever in the config is broken or moved. Since the plugin internally stores input configs using coordinates. WE commands like `//move` will lead to deletion of all input configurations on the moved circuit.

### Other input selection modes

Apart from the "slice" mode introduced above. There are another 2 modes to create an input config.

First, the `single` mode is used to control a single lever. For example, type `/ru in new ext_carry single`. Then right-click on the lever to create a config named `ext_carry`.

![1563083549324](README.assets\1563083549324.png)

All input `set` commands will apply on the single input (though it's pointless to use any advanced mode like `dec`). Besides, you can use `/ru in toggle {name}` to toggle on/off.

![1563083680188](README.assets\1563083680188.png)

(After `/ru in toggle ext_carry`)

The `multi` mode is used to select multiple inputs that aren't perfectly on the same y-level (so `slice` mode no longer works). Right click levers from the most significant bit to the least, then type `/ru doneMultiSel` to finish. (Currently right-clicking the same lever multiple times may lead to unpredicted behavior!)

![1563084021755](README.assets\1563084021755.png)

(The case to use `multi` mode).

## Outputs

### Creating output configuration

RedUtils helps you manage outputs as well. Similar to creating input configurations, type `/ru out new output1 slice` to create an output configuration named `output1` in `slice` mode. (Select from the most significant bit to the least!) (Any "powerable" block is considered a valid output in the slice, including redstone wires, repeaters..., so make sure not to select extra unwanted blocks!)

![1563084681011](README.assets\1563084681011.png)

(After the second right-click all valid output blocks will have green particles)

### Logging outputs

By default, all output configurations **actively log** any change in their state. For example, if I change the input (either manually or by `/ru in set ...`):

![1563084883220](README.assets\1563084883220.png)

This is when the `[... ticks]` in the beginning of the messages starts is useful. These ticks are measured in **GAME TICKS** (not redstone ticks). And by calculating the difference we are able to tell the exact run time of our circuit. 

If you consider large figures like `[172857 ticks]` annoying, feel free to use `/ru resetTimer` or simply `/ru rst` to reset the timer to zero.

### Formatting

But a binary output in the log is still a bit confusing. To save time, you can run `/ru out fmt {name} {display mode}` to set the display mode for an output configuration. For example, after running `/ru out fmt output1 dec`, and setting some inputs to our full adder:

![1563085347394](README.assets\1563085347394.png)

The output starts to log decimal instead of raw binary, which is very useful.

By the way, you can even find out some intermediate states of the circuit. For example, the full adder outputs 98562 for 2 ticks before outputting the correct answer 116433. This can be very useful in debugging.

There are `5` output modes, namely `bin`, `dec`, `decFrac`, `hex`, `hexPadRight`. 

Note: the difference between `hex` and `hexPadRight` is that when the output bit count is not a multiple of 4, for example, `110101`, `hex` mode will see it as `11_0101` and converts that into `0x35`, while `hexPadRight` will see it as `1101_01` and converts that into `0xD1`.

Moreover, you can use `/ru out sep {name} {interval}` to use `_` to separate every `interval` digits. For example, after setting `/ru out sep output1 3`:

![1563085905954](README.assets\1563085905954.png)

If the interval is set to `0`, no separators will be used.

If you have finished debugging your circuit, you can use `/ru out {name} log` to toggle active logging off so your chat message box is no longer spammed by all the loggings. Nevertheless, you can always use `/ru out see {name}` to manually see the value of the output (display modes apply here too).

### Deleting outputs

Similar to deleting inputs (`/ru out del {name}`), including the automatic removal of the whole configuration when any output block in the configuration is broken. To conclude, the plugin isn't WorldEdit-proof.

## Usage in command blocks

You may want to use command block to execute `/ru` based commands for precise timings. For example, set input to 5 for 2 ticks and 7 for 2 ticks and then ... to test if your circuit is robust (This is useful when pistons are involved).

You can't directly do that. Because all the configuration information is bounded to players. Different players can have different input/output configurations and logs of their own output configs will only appear on their on screen. Command Block is obviously not a player!

You can, though, use `/redutilsas {player}` command or `/ruas {player}` to let the command block execute a `/ru` command as some player. For example, `/ruas Steve in set some_input dec 1234` in a command block will have the same effect as Steve executing `/ru in set some_input dec 1234` himself. All the logs and messages will still be sent to the player's screen, not the command block's output.