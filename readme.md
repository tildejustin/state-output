## For Macro Makers and Verifiers

The **State File** is created while the game is running and can be found
in `.minecraft/wpstateout.txt`. The file contains a single line of text containing
information about the game's current state, and overwrites itself whenever the state
changes. The following states will appear as lines in the file:

- `waiting`
- `inworld,paused`
- `inworld,unpaused`
- `inworld,gamescreenopen`
- `title`
- `generating,[percent]` (before preview starts)
- `previewing,[percent]`
    - if using worldpreview
