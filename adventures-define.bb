

; GLOBAL STUFF
; --------------
Global GameMode=0		; 0 Normal In-Game
						;   
						; 5 Rucksack open	
						; 6 Rucksack open, swap selected
						; 7 Map Mode
						;
						; 8 Dialog Interchange
						
						; 10 Start Adventure Screen
						; 11 Waiting for Mouse
						; 12 In-Game Menus
						
Global OldGameMode		; (for menus etc)



; PLAYER STUFF
; -------------

Global PlayerControlMode
	; 1 - Movement
	; 2 - Stinker Movement

Global MoveCursorNewTarget; set to 36 when player clicks on a new tile as a movementgoal (and counts down to zero)
Global  MouseHeld ; MouseHeld is a counter how many ticks the mouse has been held since last released

Dim AStarOpen(10000),AStarF(10000),AStarG(10000),AStarH(10000),AStarParent(10000),AStarX(10000),AStarY(10000)
Dim AStarGrid(100,100)
Dim AStarPathNode(100)

Dim PlayerTilePathX(100), PlayerTilePathY(100)
Global PlayerTilePathPos
Dim LevelTilePlayerPathScore(100,100), LevelTilePlayerPathScoreChange(100,100)
; former is used by players as he moves: leaves trail behind him that keeps being incremented.
	; basically, the number indicates how long ago the Player was on this tile last.
	; The latter is for internal use (to avoid incrementing scores twice if player made a loop)

Global MousePickPlane ; big object spanning entire level, used for CameraPick
Global MousePickX#,MousePickY# ; where on this plane is the mouse pointing right now
Global LevelCursor, MouseCursor, MouseCursorVisible



Global DelayCommand,DelayData1,DelayData2,DelayData3,DelayData4


; LEVEL STUFF
; ------------

Dim LevelTileLogic(100,100) 
Dim ObjectTileLogic(100,100)

Global LevelTimer,AdventureTimer

Global LevelMusic,LevelWeather,Lightningstorm

Global PlayerLevelStartingYaw



; ----------------------
; Objects
; --------
; Standard Object Data
; -----------------------

Global NofObjects=0

Dim ObjectModelName$(1000)
Dim ObjectTextureName$(1000)
Dim ObjectXScale#(1000)
Dim ObjectZScale#(1000)
Dim ObjectYScale#(1000)
Dim ObjectXAdjust#(1000)
Dim ObjectZAdjust#(1000)
Dim ObjectYAdjust#(1000)
Dim ObjectPitchAdjust#(1000)
Dim ObjectYawAdjust#(1000)
Dim ObjectRollAdjust#(1000)

Dim ObjectExists(1000)	; true or false - used to delete objects at end of round

Dim ObjectEntity(1000)	; the entity
Dim ObjectTexture(1000)	; what texture is currently applied
Dim ObjectX#(1000),ObjectY#(1000), ObjectZ#(1000) ; floats of x/y/z position on field
Dim ObjectOldX#(1000),ObjectOldY#(1000),ObjectOldZ#(1000) ; from last frame
Dim ObjectDX#(1000),ObjectDY#(1000), ObjectDZ#(1000)

Dim ObjectPitch#(1000),ObjectYaw#(1000),ObjectRoll#(1000)
Dim ObjectPitch2#(1000),ObjectYaw2#(1000),ObjectRoll2#(1000)

Dim ObjectXGoal#(1000),ObjectYGoal#(1000), ObjectZGoal#(1000)
Dim ObjectMovementType(1000), ObjectMovementTypeData(1000)
Dim ObjectSpeed#(1000)
Dim ObjectRadius#(1000)
Dim ObjectRadiusType(1000) ;0-circle, 1-square

Dim ObjectData10(1000)
Dim ObjectPushDX#(1000),ObjectPushDY#(1000)

Dim ObjectAttackPower(1000),ObjectDefensePower(1000)
Dim ObjectDestructionType(1000)

Dim ObjectID(1000)
Dim ObjectType(1000)
Dim ObjectSubType(1000)

Dim ObjectActive(1000), ObjectLastActive(1000)	; 0-in active,	1001-active, odd-activating, even-deactivating
Dim ObjectActivationType(1000) ; how does Active manifest itself (fade/shrink/etc)
Dim ObjectActivationSpeed(1000) ; must be even number, 2+: how fast ObjectActive is changed

Dim ObjectStatus(1000)
Dim ObjectTimer(1000), ObjectTimerMax1(1000), ObjectTimerMax2(1000)

Dim ObjectTeleportable(1000)
Dim ObjectButtonPush(1000)
Dim ObjectWaterReact(1000)  ; 0-9	ignore water
							; 10-19	can't enter water
							; 20-29 destroyed by water
							; 30-39 float (with final height adjustment .0 to .9)
							; 40-49 sink (with final height adjustment .0 to .9)
							; 50-59 drift at depth -2 (with final height adjustment .0 to .9)
							; 60-69 drift at depth -3 (with final height adjustment .0 to .9)
							; 70-79 drift at depth -4 (with final height adjustment .0 to .9)
							; 80-89 drift at depth -5 (with final height adjustment .0 to .9)

		


Dim ObjectTelekinesisable(1000),ObjectFreezable(1000)
	; 0 - no, 1- yes, 2- yes, but currently disabled (e.g. when frozen)
Dim ObjectReactive(1000)

Dim ObjectChild(1000), ObjectParent(1000) ; for linked objects, e.g. iceblocks

Dim ObjectData(1000,10)
Dim ObjectTextData$(1000,4)

Dim ObjectTalkable(1000)
Dim ObjectCurrentAnim(1000)
Dim ObjectStandardAnim(1000)
Dim ObjectTileX(1000)
Dim ObjectTileY(1000)
Dim ObjectTileX2(1000)
Dim ObjectTileY2(1000)
Dim ObjectMovementTimer(1000)
Dim ObjectMovementSpeed(1000)
Dim ObjectMoveXGoal(1000)
Dim ObjectMoveYGoal(1000)
Dim ObjectTileTypeCollision(1000)
Dim ObjectObjectTypeCollision(1000)
Dim ObjectCaged(1000)
Dim ObjectDead(1000)
Dim ObjectDeadTimer(1000)
Dim ObjectExclamation(1000)
Dim ObjectShadow(1000)
Dim ObjectLinked(1000)
Dim ObjectLinkBack(1000)
Dim ObjectFlying(1000)
Dim ObjectFrozen(1000)
Dim ObjectIndigo(1000)
Dim ObjectFutureInt24(1000)
Dim ObjectFutureInt25(1000)
Dim ObjectScaleAdjust#(1000)
Dim ObjectScaleXAdjust#(1000)
Dim ObjectScaleYAdjust#(1000)
Dim ObjectScaleZAdjust#(1000)
Dim ObjectFutureFloat5#(1000)
Dim ObjectFutureFloat6#(1000)
Dim ObjectFutureFloat7#(1000)
Dim ObjectFutureFloat8#(1000)
Dim ObjectFutureFloat9#(1000)
Dim ObjectFutureFloat10#(1000)
Dim ObjectFutureString1$(1000)
Dim ObjectFutureString2$(1000)


; CurrentObject Used for e.g. Indigo Magic
Global CurrentObjectModelName$
Global CurrentObjectTextureName$
Global CurrentObjectXScale#
Global CurrentObjectZScale#
Global CurrentObjectYScale#
Global CurrentObjectXAdjust#
Global CurrentObjectZAdjust#
Global CurrentObjectYAdjust#
Global CurrentObjectPitchAdjust#
Global CurrentObjectYawAdjust#
Global CurrentObjectRollAdjust#
Global CurrentObjectX#,CurrentObjectY#,CurrentObjectZ#
Global CurrentObjectOldX#,CurrentObjectOldY#,CurrentObjectOldZ#
Global CurrentObjectDX#, CurrentObjectDY#, CurrentObjectDZ#
Global CurrentObjectPitch#, CurrentObjectYaw#, CurrentObjectRoll#
Global CurrentObjectPitch2#, CurrentObjectYaw2#, CurrentObjectRoll2#
Global CurrentObjectXGoal#, CurrentObjectYGoal#, CurrentObjectZGoal#
Global CurrentObjectMovementType, CurrentObjectMovementTypeData, CurrentObjectSpeed#
Global CurrentObjectRadius#, CurrentObjectRadiusType
Global CurrentObjectCollisionPower
Global CurrentObjectPushDX#, CurrentObjectPushDY#
Global CurrentObjectAttackPower, CurrentObjectDefensePower, CurrentObjectDestructionType
Global CurrentObjectID, CurrentObjectType, CurrentObjectSubType
Global CurrentObjectActive, CurrentObjectLastActive, CurrentObjectActivationType, CurrentObjectActivationSpeed
Global CurrentObjectStatus, CurrentObjectTimer, CurrentObjectTimerMax1, CurrentObjectTimerMax2
Global CurrentObjectTeleportable, CurrentObjectButtonPush, CurrentObjectWaterReact
Global CurrentObjectTelekinesisable, CurrentObjectFreezable
Global CurrentObjectReactive
Global CurrentObjectChild, CurrentObjectParent
Dim CurrentObjectData(10), CurrentObjectTextData$(4)
Global CurrentObjectTalkable,CurrentObjectCurrentAnim,CurrentObjectStandardAnim,CurrentObjectTileX,CurrentObjectTileY
Global CurrentObjectTileX2,CurrentObjectTileY2,CurrentObjectFutureInt8,CurrentObjectMovementSpeed,CurrentObjectFutureInt10
Global CurrentObjectFutureInt11,CurrentObjectFutureInt12,CurrentObjectFutureInt13,CurrentObjectFutureInt14,CurrentObjectFutureInt15
Global CurrentObjectFutureInt16,CurrentObjectExclamation,CurrentObjectShadow,CurrentObjectLinked,CurrentObjectLinkBack
Global CurrentObjectFutureInt21,CurrentObjectFrozen,CurrentObjectIndigo,CurrentObjectFutureInt24,CurrentObjectFutureInt25
Global CurrentObjectScaleAdjust#,CurrentObjectFutureFloat2#,CurrentObjectFutureFloat3#,CurrentObjectFutureFloat4#,CurrentObjectFutureFloat5#
Global CurrentObjectFutureFloat6#,CurrentObjectFutureFloat7#,CurrentObjectFutureFloat8#,CurrentObjectFutureFloat9#,CurrentObjectFutureFloat10#
Global CurrentObjectFutureString1$,CurrentObjectFutureString2$



Global CameraFocusObject ; what object is the camera following

; Specific Models
; ----------------

; Stinkers
Global StinkerMesh
Dim StinkerTexture(100,10) ; 100 styles with 10 expressions
Global StinkerSmokedTexture

; Wee Stinkers
Global NofWeeStinkersInAdventure,NofWeeStinkersFollowing, NofWeeStinkersFollowingLast
Global StinkerWeeMesh
Dim StinkerWeeTexture(3),StinkerWeeTextureSleep(3),StinkerWeeTextureSad(3)

; Cages
Global CageMesh,CageTexture

; StarGates
Global StarGateMesh

; AutoDoors
Global AutodoorMesh,AutoDoorTexture

; Buttons
Global ButtonTexture

; Gates
Global GateTexture
Global CloudTexture

; FireTrap
Global FireTrapTexture

; Teleporters
Dim TeleporterTexture(16) 

; Scritters
Global ScritterMesh
Dim ScritterTexture(7)
Global NofScrittersInAdventure

; SteppingStones
Dim SteppingStoneTexture(4)

; WaterFall
Global WaterFallMesh
Dim WaterFallTexture(2)

; Star
Global StarMesh,GoldStarTexture
Dim WispTexture(10)


; Coin
Global CoinMesh,GoldCoinTexture,TokenCoinTexture

; Key
Global KeyMesh

; Signs
Dim SignMesh(5), SignTexture(5)

; Houses
Dim DoorTexture(10),CottageTexture(10),HouseTexture(10),WindmillTexture(10),FenceTexture(10)

; Shadows
Dim ShadowTexture(10)

; Gems
Dim GemMesh(10)
Global NofGemsInAdventure
Global GemPitch

; Turtles
Global TurtleMesh, TurtleTexture

; Crabs
Global CrabMesh, CrabTexture1, CrabTexture2, NofCrabsInAdventure

; Trolls
Global TrollMesh, TrollTexture

; Kabooms
Global KaboomMesh, KaboomTextureSquint
Dim KaboomTexture(5)
Global NofBabyBoomersInAdventure

; FireFlowers
Global FireFlowerMesh,FireFlowerTexture,FireFlowerTexture2
Global NofFireFlowersInAdventure

; BurstFlowers
Global BurstFlowerMesh
Global BurstFlowerTexture

; Boxes etc
Global BarrelMesh,BarrelTexture1,BarrelTexture2,BarrelTexture3,PrismMesh,PrismTexture
Global NofBricksInAdventure

; Chompers
Global ChomperMesh,ChomperTexture,WaterChomperTexture,MechaChomperTexture

; Bowlers
Global BowlerMesh,BowlerTexture

; Busterflies
Global BusterflyMesh,BusterflyTexture

; RubberDucky
Global Rubberduckymesh,rubberduckytexture

; Spring
Global SpringTexture

; void
Global voidtexture
; Thwarts
Global ThwartMesh
Dim ThwartTexture(9)

; FlipBridges
Global IsThereAFlipBridge

; Tentacles
Global TentacleMesh,TentacleTexture

; GrowFlowers, Floingbubbles
Global GlobalGrowFlowerCounter, GlobalFloingBubbleCounter
Global PlasmaTexture, FLoingTexture
Global NofFloingBubbles

; Retrostuff
Global RetroBoxMesh,RetroBoxTexture,RetroCoilyMesh,RetroCoilyTexture,RetroScougeMesh,RetroScougeTexture
Global RetroUfoMesh,RetroUfoTexture,RetroZbotMesh,RetroZbotTexture,RetroRainbowCoinTexture

;ZBots
Global WeebotMesh, WeebotTexture
Global ZapbotMesh, ZapbotTexture
Global PushbotMesh, PushbotTexture
Global ZbotNPCMesh
Dim ZbotNPCTexture(8)
Global NofZBotNPCsInAdventure
Global MothershipMesh, Mothershiptexture
Global NofZBotsInAdventure

;Portal
Global PortalWarpMesh,StarTexture,RainbowTexture,RainbowTexture2

; Lurker
Global LurkerMesh,LurkerTexture

; Ghosts
Global GhostMesh,WraithMesh,GhostTexture
Dim WraithTexture(3)





; Mirror
Dim MirrorTexture(6)
Global SkyMachineMapTexture

; Obstacles
Dim ObstacleMesh(100),ObstacleTexture(100)
Dim Mushroomtex(3)

; V1.04 Preloads
Global Cylinder,Fence1,Fence2,FencePost,Door01b3d,Door013ds,Fountain
Global Adventuretitlestar, Adventuretitlestartex1,AdventureTitlestartex2
Global AdventureStartlogotex,AdventureCompletedLogoTex,AdventureLostLogoTex
Global Square,Firetraptex,GloveTex
Global townhouse01a,townhouse01b,townhouse02a,townhouse02b,cottage

; Players
Const maxplayers=1

Global PlayerObject ; which object is currently the playerobject (-1 - none)
Global StinkerObject ; which object is currently a controlled stinker (-1 - none)
Global PlayerTalkToGoalObject ; if player is en route to talk to an object

Global LastPlayerControl ; what was being done with the mouse in the last frame
							


Global PlayerName$,OldPlayerName$
Global PlayerCharacterName$,OldPlayerCharacterName$,OldPlayerCharacterName2$

Global PlayerTextureBody=Rand(1,4)
Global PlayerAcc1=Rand(0,22)
Global PlayerTexAcc1=1
Global PlayerAcc2=Rand(0,2)
Global PlayerTexAcc2=1
Global PlayerSizeX#=0.035
Global PlayerSizeY#=0.035
Global PlayerSizeZ#=0.035
Global PlayerVoice=1
Global PlayerPitch




Dim AdventureCompleted(500),AdventureCompletedTime(500),AdventureCompletedGems(500),AdventureCompletedGemsTotal(500),AdventureCompletedCoins(500),AdventureCompletedCoinsTotal(500),AdventureCompletedScore(500)
Global CurrentAdventureGems, CurrentAdventureCoins, CurrentAdventureTime, CurrentAdventureScore
Global ReplayAdventureGemsBetter,ReplayAdventureCoinsBetter,ReplayAdventureScoreBetter
Global PreReplayAdventureLevel,PreReplayAdventureX,PreRePlayAdventureY

Global CurrentReplayAdventure=0
Global CurrentReplayAdventureName$

Dim Mappiecefound(8), MysteryNumber(5)
Global mysterynumberpos
Global Wa3BlueFlower ; which message they'll get
Global Wa3BlueFlowerStatus ;(0-nothing yet,1-got secret message (flower can appear), 2-got flower)


; Inventory
Global PlayerCoins, PlayerCoinsCollected, PlayerGems, PlayerStars, PlayerScore

Global InventorySize=3

Global NofInventoryItems

Dim InventoryItem(100)
Dim InventoryID(100), InventoryTexture(100)
Dim InventoryHelpText$(100),InventorySubText$(100)
Global InventorySwapItem,InventorySwapIcon

Global ShardsAreActive=False
Global ShardHitCounter,ShardLastHit, ShardMelodyTimer ;(not saved in player)
Dim ShardMelody(7)

; Before Adventure
Global PlayerCoinsB4, PlayerCoinsCollectedB4, PlayerGemsB4, PlayerStarsB4, PlayerScoreB4,LightPowerB4

Global InventorySizeB4

Global NofInventoryItemsB4

Dim InventoryItemB4(100)
Dim InventoryIDB4(100), InventoryTextureB4(100)
Dim InventoryHelpTextB4$(100),InventorySubTextB4$(100)





; Adventure Data
Global NofHubs=2				; how many hubs - easy for adding later expansions
Global AdventureCurrentLevel	; >0 Adventure (standard or custom), <0 Hub
Global AdventureCurrentStatus	; 0-hub, 1-adventure, 2-custom adventure, 3-replay adventure 
Global AdventureCurrentNumber	; if a regular adventure (set to zero for custom)
Global AdventureCurrentName$	; if a custom adventure

;Global AdventureName$
Global AdventureStartX,AdventureStartY ; x/y position of player start
Global GateKeyVersion=1
Global AdventureTitle$
Dim AdventureTextLine$(5)
Dim AdventureTitleSprite(4)	; actual entities: 0 - logo, 1/2/3 - help sprites
Dim AdventureTitleSpriteTex(4) ; associated textures
Dim AdventureHelpSprite$(3) ; the name of the texture for the 3 help sprites
Dim AdventureHelpLine$(3) ; the textline just underneath it
Global AdventureTitleBackground,AdventureTitleBackgroundTex, AdventureTitleBackgroundTexName$
;Global AdventureTitleStar,AdventureTitleStarTex

Global AdventureExitWonLevel, AdventureExitWonX, AdventureExitWonY ; at what hub level and x/y do you reappear if won.
Global AdventureExitLostLevel, AdventureExitLostX, AdventureExitLostY ; at what hub level and x/y do you reappear if won.


Global AdventureGoal	; when is adventure done
						; 1-NofWeeStinkersInAdventure=0

Dim AdventureWonCommand(3,6)	; 3 commands, each with level/command/fourdata
						
Global AdventureLostReason$, AdventureLostCameraX#,AdventureLostCameraY#,AdventureLostCameraZ#


; Spells
Global SpellActive=False ; True - control now used for spells
Global CurrentSpell=-1	; What charge do the gloves have: -1-none, 0-power, 1or2-fire, 3-   , 4-,   5-ice, 6-water, 7-teleport, 8-  
Global CurrentCharm=0	; 1-vision, 2-light
Global CurrentSpellPower=0 ; how many 'charges' remain
Global CurrentLightPower=0
Global IndigoActive=0	; 0-no indigo cast
						; 1-first spellball (grabber) in air
						; 2-object indigo'd started/in progress (e.g. if object still moving)
						; 3-object indigo'd complete
						; 4-object inidgo'd and next spellball (placer) in air

Global PlayerLavaTimer=0


; 0-nothing, 1-telekinesis, 2-ice
Global TelekinesisActive, TelekinesisObject
Global IceSpellTimer,TelekinesisSpellTimer

Global PlayerCancelMove