# Changelog

## v3.0.0 -> 3.1.0

### Breaking Changes
- Renamed `WorldDateTime.WORLD_TO_SECONDS` to `WORLD_TO_REAL_SECONDS`
- Changed all hour references in `WorldDateTime` to fix for ticks starting at 6am on day 0 instead of midnight
- Renamed constants in `ConsoleMEssageColors` `WARNCOLOR`, `ERRORCOLOR`, and `RESETCOLOR` to `WARN_COLOR`, `ERROR_COLOR`, and `RESET_COLOR`

### Modifications
- Added `WorldDateTime.TICKS_TO_WORLD_HOURS` and `WorldDateTime.TICKS_TO_WORLD_MINUTES`
- Added `WorldDateTime.setHourTicks(long): WorldDateTime`
- Added `WorldDateTime.setMinute(int): WorldDateTime`
- Fixed `WorldDateTime.formattedTime(): String` to return a time formatted as `DD HH:MM` instead of `DD:MM`
- And lots of spelling fixes, cause apparently my other dev environment wasn't giving me spell check

## v2.2.2 -> v3.0.0

Updated to MC 1.21.4

Added `IngameTimer` for timing based on ingame time

### Breaking Changes

- Moved all classes from `peterTimer` to `com.peter.petertimer`
- `Timer`
    - Removed deprecated constructor `Timer(int, String, Map<Integer,TimeRunnable>, boolean, JavaPlugin)`
    - Removed deprecated constructor `Timer(int, Map<Integer,TimeRunnable>, boolean, JavaPlugin)`
    - Removed function `getTicksRemaning(): int` -> Replaced with `getTime(): int` from `AbstractTimer`
    - Removed function `getSecondsRemaning(): int` -> Replaced with `getSeconds(): float` from `AbstractTimer`
    - Removed deprecated function `setName(String)`
    - Changed visibility of `update(int)` from `public` to `protected`
- `TimeRunnable`
    - Changed signature of `TimeRunnable.run(Timer)` to `TimeRunnable.run(AbstractTimer)`

### Modifications
- Added `AbstractTimer`
    - Functions moved from `Timer` to `AbstractTimer`
        - `getTicksRemainig(): int` -> `abstract getTime(): int`
        - `getSecondsRemaning(): int` -> `getSeconds(): float`
        - `getName(): String`
        - `setTitle(String)` & `setTitle(String,String)`
        - `setAutoChange(boolean)`
        - `start()`
        - `stop()`
        - `addAllPlayers()` & `addAllPlayers(String)`
        - `addPlayer(Player)`, `addPlayer(Player, String)`, `addPlayer(List<Player>)` & `addPlayer(List<Player>, String)`
        - `removeAllPlayers()` & `removeAllPlayers(String)`
        - `removePlayer(Player)`, `removePlayer(Player, String)`, `removePlayer(List<Player>)` & `removePlayer(List<Player>, String)`
        - `setColor(BarColor)` & `setColor(BarColor, String)`
        - `setStyle(BarStyle)` & `setStyle(BarStyle, String)`
        - `addBar(String)`
        - `getBar(String): DisplayBar`
        - `removeBar(String)`
        - `addCallback(int, TimeRunnable)`
        - `removeCallback(int): boolean`
        - `public update(int)` -> `protected abstract update(int)`
        - `private format(int): String` -> `protected format(int): String`
    - Functions added to `AbstractTimer`
        - `setCallbacks(Map<Integer,TimeRunnable>)`
        - `abstract getTime(): int`
        - `getSeconds(): float`
        - `protected calcMaxBetween(int)`
        - `protected updateBar(int, int)`
        - `protected scheduleNext(int)`
- `Timer` now inherits from `AbstractTimer`
- Added `IngameTimer`
    - Runs till a specified in-game time
    - Constructors
        - `IngameTimer(String name, JavaPlugin plugin, long endTime, World world)`
        - `IngameTimer(String name, JavaPlugin plugin, WorldDateTime endTime)`
    - Methods
        - `setEndTime(WorldDateTime endTime)`
        - `getEndTime(): WorldDateTime`
- Added `WorldDateTimer`
    - Represents an in-world time
    - Static constructors
        - `WorldDateTime.current(World world): WorldDateTime`
        - `WorldDateTime.of(long worldTime, World world): WorldDateTime`
    - Methods
        - `setTicks(long ticks): WorldDateTime`
        - `setHour(long hour): WorldDateTime`
        - `setDay(long day): WorldDateTime`
        - `getTicks(): long`
        - `getHour(): long`
        - `getDay(): long`
        - `addTicks(long ticks): WorldDateTime`
        - `addHour(long hours): WorldDateTime`
        - `addDay(long days): WorldDateTime`
        - `ticksTill(): long` & `ticksTill(long currentWorldTime): long`
        - `secondsTill(): float` & `secondsTill(long currentWorldTime): float`
        - `formattedTime(): String`
        - `copy(): WordDateTime`
    - Fields
        - `World world`
    - Constants
        - `float WORLD_TO_SECONDS = (20 * 60) / (1000 * 24)`
- Added `TimeRunnableExact`
    - Extends `TimeRunnable`
    - If passed to `IngameTimer` will only execute if the requested time is hit exactly and not if the time was skipped
