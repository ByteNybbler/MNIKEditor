

Function ResetSounds()

	For i=0 To 500
		SoundFXVolume(i)=-1
	Next
End Function

Function PlaySoundFX(i,x,y);,pitch)

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