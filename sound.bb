

Function ResetSounds()

	For i=0 To 500
		SoundFXVolume(i)=-1
	Next
End Function

Function PlaySoundFX(i,x,y);,pitch)

	If i<0
		Return
	EndIf

;	If pitch>-1
		

	; play full blast volume sound
	If x=-1 And y=-1
		SoundFXVolume(i)=1.0*GlobalSoundVolume
		Return
	EndIf
	
	; play positional sound - base volume on distance to character
	; (later - take into account moved camera?)
	
	Dist# = (x-EntityX(Camera1))^2 + (y-(-EntityZ(Camera1)-10))^2
	
	If dist<16.0
		SoundFXVolume(i)=1.0*GlobalSoundVolume
	Else
		vol#=(1.0-(dist-9.0)/64.0)*GlobalSoundVolume
		If vol#>0 And vol#>SoundFXVolume(i)
			SoundFXVolume(i)=vol
		EndIf
	EndIf
	
	
End Function

Function PlaySoundFXNow(i)
	SoundVolume SoundFX(i),GlobalSoundVolume
	PlaySound (SoundFX(i))
End Function

Function PlayAllSounds()

	For i=0 To 500
		If SoundFXVolume(i)>0
		
			SoundVolume SoundFX(i),SoundFXVolume(i)
			If i=11 
				SoundPitch SoundFX(i),20000+GemPitch/8.0*20000.0;Rnd(19000,25000)
				GemPitch=(GemPitch+1) Mod 9
			EndIf
			
			If i=111 SoundPitch SoundFX(i),18000+Rand(0,8000)
			PlaySound (SoundFX(i))
		EndIf
	Next
End Function


Function LoadSounds()

	Restore SoundFXNames
	For i=0 To 159
;		If i=50
;			PositionEntity cube2,.8,-1,4
;			RenderWorld
;			Flip
;		EndIf
;		If i=110
;			PositionEntity cube2,.9,-1,4
;			RenderWorld
;			Flip
;		EndIf
		Read a$
		If a$<>"---"
			
			SoundFX(i)=myLoadSound("data\sound\"+a$+".wav")
			
			
		
			
			
			
		EndIf

	Next
	
	For i=160 To 186
		Read a$
	Next
	
	For i=187 To 199
		
		Read a$
		If a$<>"---"
			
			SoundFX(i)=myLoadSound("data\sound\"+a$+".wav")
			
			
		
			
			
			
		EndIf

	Next

	;PlaySoundFX(21,-1,-1)

	For i=200 To 209
		
		Read a$
		If a$<>"---"
			
			SoundFX(i)=myLoadSound("data\sound\"+a$+".wav")
			;debuglog "sound "+a$+" loaded at "+i
			
		
			
			
			
		EndIf

	Next

End Function



.SoundFXNames
;0
Data "adventurewon1","adventurewon2","---","---","---","---","---","---","---","---"
;10
Data "pinball2","pickupjewel","pickupcoin2","shard","wakka","explosion","electricshock","---","---","---"
;20
Data "ice","buttonoff","buttonturn","buttontimer","colourchange","---","---","---","ghost","wraith"
;30
Data "fireon2","fireduring2","cageclose","bridgeup","bridgedown","gateon1","gateoff1","gatedoor","autodooropen","autodoorclose"
;40
Data "transporter","transporterflip","teleport","suctubein","suctubeout","flystop","---","---","---","---"
;50
Data "weehello1","weehello2","weehello3","weehello4","weehello5","weeyes1","weeyes2","weeyes3","weeyes4","weezee"
;60
Data "weeok1","weeok2","weeok3","weeok4","weeok5","weeouch1","weeouch2","weeouch3","weebored","weetired"
;70
Data "weewoo","weethanks","weebye","weeyeay","kaboom1","kaboom2","kaboom3","kaboom4","kaboom5","kaboom6"
;80
Data "magiccharge","magicblink2","magiccast","freeze","freezebreak","freezestinker","freezechomper","freezethwart","spellballbounce","---"
;90
Data "magicon","magicoff","grow","floingbubble","---","saucerfly","saucerdie","lurker","moobot1","moobot2"
;100
Data "scritterhop","chomper","flowergrow","flowerfire","flowerhit","flowerdie","thwart","thwartpickup","splosh","spikeyball"
;110
Data "brick","tentacleup","tentacledown","trollwalk","trollice","crabwalk","crabup","crabdown","bounce","chomper2"
;120
Data "waterfall","quack","quake","void","bubble","splash","surf1","surf2","seagull1","seagull2"
;130
Data "menuclick","invenopen","invenclose","loadgame","savegame","areyousure","menuclick2","loadinggame","mystery1","mystery2"
;140
Data "zbot10","zbot1","zbot2","zbot3","zbot4","zbot5","zbot6","zbot7","zbot8","zbot9"
;150
Data "voices\death1","voices\death2","voices\death3","voices\hot1","voices\hot2","lightning1","lightning2","lightning3","zbot11","zbot12"
;160
Data "lost1","lost2","lost3","lost4","start1","start2","start3","start4","start5","hello"
;170
Data "ice1","ice2","ice3","coin","item","item-no","aha","intro","---","---"
;180
Data "suc1","suc2","suc3","---","---","---","---","voices\1\extra1","voices\1\extra2","voices\1\extra3"
;190
Data "voices\1\hello","voices\1\intro","voices\2\hello","voices\2\intro","voices\3\hello","voices\3\intro","voices\4\hello","voices\4\coin","voices\3\extra1","voices\3\extra2"
;200
Data "---","---","---","---","---","---","---","---","---","---"
;Data "inflate","prism","---","---","---","---","---","---","---","---"