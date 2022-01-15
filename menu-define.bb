;WA3 Cutscene Stuff
Global StartofTitleMusic


; Custom Level Stuff
Global InCustomHub  ; 0-no, 1-yes
Global InCustomHubName$ ; points to directory
Global InCustomHubIconName$ ; custom icon name for entire hub
Global InCustomHubNameTruncated$ ; just the name (without directory or subtitle)

Global CustomCurrentArchive=0 ; 0-current, 1-archive
Global CustomLevelListStart=0
Global CustomLevelListSelected=-1 ; -1: none selected
Dim CustomLevelListFileName$(5000)
Dim CustomLevelListName$(5000)
Dim CustomLevelListCreator$(5000)
Dim CustomLevelListCompleted(5000)
Dim CustomLevelListScore(5000)
Dim CustomLevelListGems(5000)
Dim CustomLevelListGemsTotal(5000)
Dim CustomLevelListCoins(5000)
Dim CustomLevelListCoinsTotal(5000)
Global NofCustomLevels

Dim CustomHubListFileName$(5000)
Dim CustomHubListName$(5000)
Dim CustomHubListSubTitle$(5000)
Global NofCustomHubs

Global NofCustomTags
Dim CustomTag$(1000)
Global CurrentCustomTag$="Not Archived"
Global CurrentCustomTagNumber=-2
Global NewTagNameEntered$


Global SaveSlotImage,SaveSlotImage2
Global AdventureTitle4Saving$
Dim SaveSlotEntity(9),SaveSlotTexture(9),SaveSlotDateTime$(9),SaveSlotLevelName$(9)

Global DialogBackGroundEntity,DialogBackGroundEntity2,DialogBackGroundSize


; Dialog
Global CurrentDialog, CurrentInterchange, CurrentInterChangeNofLines, CurrentInterChangeReply
Global DialogTimer, DialogLineLength
Global DialogCurrentRed,DialogCurrentGreen,DialogCurrentBlue,DialogCurrentEffect

Global DialogObject1,DialogObject2

Const MaxInterChanges=100
Global StartingInterChange
Global NofInterchanges	
Dim NofInterChangeTextLines(MaxInterChanges)	
Dim InterChangeTextLine$(MaxInterChanges,7)
Dim DialogTextCommand$(MaxInterChanges,200),DialogTextCommandPos(MaxInterChanges,200),NofTextCommands(MaxInterChanges)	

Dim NofInterChangeReplies(MaxInterChanges)
Dim InterChangeReplyText$(MaxInterChanges,8)
Dim InterChangeReplyFunction(MaxInterChanges,8)
Dim InterChangeReplyData(MaxInterChanges,8)
Dim InterChangeReplyCommand(MaxInterChanges,8)
Dim InterChangeReplyCommandData(MaxInterChanges,8,4)
Const MaxAskAbouts=100
Global NofAskAbouts
Global AskAboutTopText$
Dim AskAboutText$(MaxAskAbouts)
Dim AskAboutActive(MaxAskAbouts)
Dim AskAboutInterchange(MaxAskAbouts)
Dim AskAboutRepeat(MaxAskAbouts)



; AskAbout Masterlist
Global NofMasterAskAbouts
Dim MasterAskAboutActive(1000)

Dim DialogTextLine$(10)


; Icons (in-game)

; There are 80 icons, from x=0-9 and y=0-7

Global IconTextureStandard, IconTextureCustom

Dim IconEntity(100)
Dim IconX#(100),IconY#(100)
Dim IconTexture(100)
Dim IconSize(100) ;0%-100%, for fade in-out
Dim IconType(100)
Dim IconHelpText$(100),IconSubText$(100)


Global IconPicked=-1 ; -1 if none, otherwise 0 to 79


Dim TitleMenuEntity(100),TitleMenuTexture(100)
Global Titlemenuflag,Titlemenupointat

Global ClickonUnwalkable=False
Global UsedInventoryOnce=False


Global MessageLineText1$,Messagelinetext2$,MessageLineTimer

Global DialogContrast

Global CurrentMenu, CurrentMenuNofItems, MenuTimer, MenuTime
Dim MenuText$(30,10)
Dim MenuActive(30,10)

Restore Menutexts
For i=0 To 26
	For j=0 To 9
		Read menutext$(i,j)
		
		
	Next
Next

Global GameSlotToBeSaved,GameNameToBeSaved$

Global Profile2BDeleted$

Dim MapPieceEntity(8), MapPieceFound(8), MapPieceTexture(9)


Dim Credittext$(200),CreditColour(200)
Global credittimer
Restore Credittext
For i=0 To 199
	Read credittext$(i)
	Read creditcolour(i)
Next

.MenuTexts


; In Game

; 0 in-game Main Menu
Data "Resume Game","Load Game","Save Game","Restart Adventure","Abort Adventure","Exit Game","","","",""

; 1 abort adventure - are you sure
Data "Abort Adventure","Are You Sure?","-------------","Yes, Please","No, Thanks!","","","","",""

; 2 restart adventure - are you sure
Data "Restart Adventure","Are You Sure?","-------------","Yes, Please","No, Thanks!","","","","",""

; 3 exit game - are you sure
Data "Exit Game","Are You Sure?","-------------","Yes, Please","No, Thanks!","","","","",""

; 4 save game
Data "Save Game","Select A Save Slot:","","","","","","","","Cancel"

; 5 load game
Data "Load Game","Select A Save Slot:","","","","","","","","Cancel"

; 6 overwrite game
Data "Overwrite Game","sss","Are You Sure?","-------------","Yes, Please","No, Thanks!","","","",""

; 7 save before exit?
Data "Do you want to","exit without saving","your game?","","Yes, that's fine.","No, save my game!","","","",""




; 8 Floinging
Data "","","","","","","","","",""

Data "","","","","","","","","","" ; in-game not used
Data "","","","","","","","","","" ; in-game not used


; Out of Game


; 11 - main menu
Data "","","","Play Custom Adventure","Play Custom Hub","Load Saved Game","Options","Exit Game","",""
; 12 - select profile
Data "Select Player Profile:","","","","","","","","","Create New Profile"
; 13 - enter new profile name
Data "Please Enter Your Name:","","","","OK","Cancel","","","",""
; 14 - select character
Data "Select A Character:","","","","","","","","","."
; 15 - create custom character
Data "Create Your Character","","","","","","","","Done","Back"
; 16 - load game from main menu
Data "Load Game","Select Saved Game:","","","","","","","","Cancel"
; 17 - profile name already exists
Data "This Profile","Already Exists!","","Please Enter","A Different Name.","","OK","","",""
; 18 - options
Data "Options","","Change Player","Mouse Control","Sound Volume: 5","Music Volume: 3","Text: Regular","Credits and Notes","Back",""
; 19 exit to windows - are you sure
Data "Exit to Windows","Are You Sure?","-------------","Yes, Please","No, Thanks!","","","","",""
; 20 - delete profile
Data "Delete Player Profile","Are You Sure?","-------------","Yes, Please","No, Thanks!","","","","",""
; 21 - cutscene
Data "","","","","","","","","",""
; 22 - cutscene
Data "","","","","","","","","",""
; 23 - cutscene
Data "","","","","","","","","",""
; 24 - Floinging
Data "","","","","","","","","",""
; 25 - Trailer
Data "","","","","","","","","",""
; 26 - credits
Data "Credits","-------","","","","","","","","Back"


; Credit Text
.CreditText
Data "",0,"",0,"",0,"",0,"",0,"",0,"",0,"",0,"",0,"",0
Data "Wonderland",0
Data "Custom Adventures",0
Data "",0
Data "A MIDNIGHT SYNERGY Production",0
Data "",0
Data "Design, Programming, Audiovisuals",0
Data "Patrick Maidorn",1
Data "",0
Data "Additional Modding Support",0
Data "Aryan",1
Data "ALEXALEX976",1
Data "HumanGamer",1
Data "Sammy_Bro",1
Data "",0
Data "Please visit the Wonderland Forum",0
Data "to read detailed information on",0
Data "the fair-use rules of the editor,",0
Data "get assisatnce in using the editor,",0
Data "and to upload/download custom content.",0
Data "",0
Data "Do not distribute this editor or any",0
Data "content created with it.",0
Data "",0
Data "It is exclusively for members of the",0
Data "Wonderland Forum community.",0
Data "",0
Data "Thank you and Enjoy!",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "The following credits are for the",1
Data "Wonderland Adventure games and the",1
Data "material contained within the editor.",1
Data "",0
Data "Design, Programming, Audiovisuals",0
Data "Patrick Maidorn",1
Data "",0
Data "Musical Score",0
Data "Dan Reynolds",1
Data "Jonne Valtonen",1
Data "",0
Data "Additional 3D Models",0
Data "James Abraham",1
Data "",0
Data "Software Development Consultant",0
Data "Claire Polster",1
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "Special Thanks to the many community",1
Data "members that have helped out to create",1
Data "these games, including content creation,",1
Data "testing, and much, much more!",1
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "And, no, there is no easter egg",0
Data "in the credits!",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "Really!",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "Unlesss you really like numbers,",0
Data "in which case you might like",0
Data "the number 02987.",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0
Data "",0

.Flowertext
Data 1,"A.FAL/O/WSE",1
Data 2,"R.GDR/O/WDS",1
Data 3,"F.ODR/O/NFE",1
Data 1,"M.ISN/U/TSE",2
Data 2,"E.VFE/R/YGN",2
Data 3,"O.OSN/A/NSD",2
Data 1,"M.IFD/N/IEG",3
Data 2,"H.TSI/F/YQO",3
Data 3,"U.PFL/A/NST",3
Data 1,"I.TDO/N/BGA",4
Data 2,"R.RSE/N/SSF",4
Data 3,"A.CFE/N/EDW",4
Data 1,"L.ISF/E/WFI",5
Data 2,"L.LFS/T/ASR",5
Data 3,"T.TSH/E/REE",5

