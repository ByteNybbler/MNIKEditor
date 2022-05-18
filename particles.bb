; ********************************************
;
; Single Surface Particle System Functions
;
; ********************************************


Function ResetParticles(texfile$)

	; Reset all ParticleMeshes and Clears all particles, then creates new mesh with texture
	;
	; Could be called once e.g. every level startup, or at the end to cleanup


	; cam = 0 , not bound to camera, 	cam=1, create as a child of camera
		
;	If ParticleTexture<>0
;		FreeTexture ParticleTexture
;		ParticleTexture=0
;	EndIf
	If ParticleMesh<>0
		FreeEntity ParticleMesh
		ParticleMesh=0
	EndIf
	If ParticleMesh2<>0
		FreeEntity ParticleMesh2
		ParticleMesh2=0
	EndIf

	
	NofParticles=0
	NofParticles2=0
	
	For p.particle = Each particle
		Delete p
	Next
	For p2.particle2 = Each particle2
		Delete p2
	Next

	
	
	ParticleMesh=CreateMesh()
	ParticleMesh2=CreateMesh(camera)
	
	ParticleSurface=CreateSurface(ParticleMesh)
	ParticleSurface2=CreateSurface(ParticleMesh2)
	
	EntityFX ParticleMesh,35 ; use 32+1+2=35 for alpha
	EntityBlend ParticleMesh,3
;	ParticleTexture=myLoadTexture(texfile$,1)
	EntityTexture ParticleMesh,ParticleTexture

	EntityFX ParticleMesh2,35 ; use 32+1+2=35 for alpha
	EntityBlend ParticleMesh2,3
	EntityTexture ParticleMesh2,ParticleTexture
	EntityOrder ParticleMesh2,-100


	
	SavedTFormedPitch=-9838993.2356
	ParticleFixedCameraCheck()
	
	
End Function


	

Function AddParticle(tex,x#,y#,z#,angle#,size#,dx#,dy#,dz#,dangle#,dsize#,ddx#,ddy#,ddz#,LifeSpan,Orient)

	If NofParticles=MaxNofParticles
		;ShowMessage("We're hitting the ceiling, boys! NofParticles="+NofParticles,1000)
		Return
	EndIf

	;Texture	; what cell # in the texture (e.g. 0-63 in an 8x8)
	;x#,y#,z#	; x/y/z position of center
	;angle#		; angle (0-359 degrees), rotated clockwise
	;size#		; size in units (i.e. diameter)
	;dx#,dy#,dz#; dx/dy/dz velocity 
	;dangle#	; rotation speed (in degrees, clockwise)
	;dsize#	; change in size
	;ddx,ddy,ddz; ddx/ddy/ddz acceleration
	;LifeSpan	; how many cycles does it stick around. Zero = infinite life
	;Orient		; 0- point to camera, 1- vary only horizontally toward camera, 2- no change (stay in x/y plane)
				; 3- all with one Tform angle toward camera (best!),	4- x/z plane,   5- y/z plane
				


	p.particle=New particle
	p\Texture=tex
	p\x=x
	p\y=y
	p\z=z
	p\angle=angle
	p\size=size/2.0	; radius, hence divide "diameter" by 2 
	p\dx=dx	
	p\dy=dy
	p\dz=dz
	p\dangle=dangle
	p\dsize=dsize
	p\ddx=ddx
	p\ddy=ddy
	p\ddz=ddz
	p\LifeSpan=LifeSpan
	p\Timer=LifeSpan ; internal use - clocks down until zero
	p\Orient=Orient
	
	p\Change=0 ; internal use - decide in advance what variables will need updating to speed up
	If p\dx<>0 Or p\dy<>0 Or p\dz<>0
		p\Change=1
		If p\dangle<>0 Or p\dsize<>0
			p\Change=p\Change+2
		EndIf
	EndIf
	If p\ddx<>0 Or p\ddy<>0 Or p\ddz<>0
		p\Change=2
		If p\dangle<>0 Or p\dsize<>0
			p\Change=p\Change+2
		EndIf
	EndIf
	If p\Change=0 And (p\dangle<>0 Or p\dsize<>0)
		p\Change=5
	EndIf

	NofParticles=NofParticles+1
	
End Function

Function AddParticle2(tex,x#,y#,z#,angle#,size#,dx#,dy#,dz#,dangle#,dsize#,ddx#,ddy#,ddz#,LifeSpan,Orient)

	;Texture	; what cell # in the texture (e.g. 0-63 in an 8x8)
	;x#,y#,z#	; x/y/z position of center
	;angle#		; angle (0-359 degrees), rotated clockwise
	;size#		; size in units (i.e. diameter)
	;dx#,dy#,dz#; dx/dy/dz velocity 
	;dangle#	; rotation speed (in degrees, clockwise)
	;dsize#	; change in size
	;ddx,ddy,ddz; ddx/ddy/ddz acceleration
	;LifeSpan	; how many cycles does it stick around. Zero = infinite life
	;Orient		; 0- point to camera, 1- vary only horizontally toward camera, 2- no change (stay in x/y plane _USE THIS ONE FOR CAMERA!)
				; 3- all with one Tform angle toward camera (best!),	4- x/z plane,   5- y/z plane
				


	p.particle2=New particle2
	p\Texture=tex
	p\x=x
	p\y=y
	p\z=z
	p\angle=angle
	p\size=size/2.0	; radius, hence divide "diameter" by 2 
	p\dx=dx	
	p\dy=dy
	p\dz=dz
	p\dangle=dangle
	p\dsize=dsize
	p\ddx=ddx
	p\ddy=ddy
	p\ddz=ddz
	p\LifeSpan=LifeSpan
	p\Timer=LifeSpan ; internal use - clocks down until zero
	p\Orient=Orient
	
	p\Change=0 ; internal use - decide in advance what variables will need updating to speed up
	If p\dx<>0 Or p\dy<>0 Or p\dz<>0
		p\Change=1
		If p\dangle<>0 Or p\dsize<>0
			p\Change=p\Change+2
		EndIf
	EndIf
	If p\ddx<>0 Or p\ddy<>0 Or p\ddz<>0
		p\Change=2
		If p\dangle<>0 Or p\dsize<>0
			p\Change=p\Change+2
		EndIf
	EndIf
	If p\Change=0 And (p\dangle<>0 Or p\dsize<>0)
		p\Change=5
	EndIf

	NofParticles2=NofParticles2+1
	
End Function



Function ControlParticles()

	; deals with particles once set in motion (no individual control possible)
	; should be called once every cycle
	
	For p.particle = Each particle
				
		Select p\Change ; pre-determined what needs to be updated
		Case 0:
			; no action (other than fading out)
		Case 1:	
			; Bare-Bones straight movement
			; no rotate/resize/accelerate
			
			; smoke, fire, etc.
			
			p\x=p\x+p\dx
			p\y=p\y+p\dy
			p\z=p\z+p\dz
		
		Case 2:
			; parabolic movement
			; change velocity (acceleration)
			p\dx=p\dx+p\ddx
			p\dy=p\dy+p\ddy
			p\dz=p\dz+p\ddz
			p\x=p\x+p\dx
			p\y=p\y+p\dy
			p\z=p\z+p\dz
			
		Case 3:	
			; straight movement
			; with rotation
			p\x=p\x+p\dx
			p\y=p\y+p\dy
			p\z=p\z+p\dz
			p\angle=p\angle+p\dangle
			p\size=p\size+p\dsize
		
		Case 4:
			; parabolic movement
			; change velocity (acceleration), with rotation
			p\dx=p\dx+p\ddx
			p\dy=p\dy+p\ddy
			p\dz=p\dz+p\ddz
			p\x=p\x+p\dx
			p\y=p\y+p\dy
			p\z=p\z+p\dz
			p\angle=p\angle+p\dangle
			p\size=p\size+p\dsize
		Case 5:	
			; stationary but rotating/resizing
			p\angle=p\angle+p\dangle
			p\size=p\size+p\dsize

		End Select
			
		;phase out and lifetime 
		If p\LifeSpan>0		
			p\Timer=p\Timer-1		
			If p\Timer=0 
				Delete p
				NofParticles=NofParticles-1
			EndIf
		EndIf
				
	Next
	
	
	For p2.particle2 = Each particle2
				
		Select p2\Change ; pre-determined what needs to be updated
		Case 0:
			; no action (other than fading out)
		Case 1:	
			; Bare-Bones straight movement
			; no rotate/resize/accelerate
			
			; smoke, fire, etc.
			
			p2\x=p2\x+p2\dx
			p2\y=p2\y+p2\dy
			p2\z=p2\z+p2\dz
		
		Case 2:
			; parabolic movement
			; change velocity (acceleration)
			p2\dx=p2\dx+p2\ddx
			p2\dy=p2\dy+p2\ddy
			p2\dz=p2\dz+p2\ddz
			p2\x=p2\x+p2\dx
			p2\y=p2\y+p2\dy
			p2\z=p2\z+p2\dz
			
		Case 3:	
			; straight movement
			; with rotation
			p2\x=p2\x+p2\dx
			p2\y=p2\y+p2\dy
			p2\z=p2\z+p2\dz
			p2\angle=p2\angle+p2\dangle
			p2\size=p2\size+p2\dsize
		
		Case 4:
			; parabolic movement
			; change velocity (acceleration), with rotation
			p2\dx=p2\dx+p2\ddx
			p2\dy=p2\dy+p2\ddy
			p2\dz=p2\dz+p2\ddz
			p2\x=p2\x+p2\dx
			p2\y=p2\y+p2\dy
			p2\z=p2\z+p2\dz
			p2\angle=p2\angle+p2\dangle
			p2\size=p2\size+p2\dsize
		Case 5:	
			; stationary but rotating/resizing
			p2\angle=p2\angle+p2\dangle
			p2\size=p2\size+p2\dsize

		End Select
			
		;phase out and lifetime 
		If p2\LifeSpan>0		
			p2\Timer=p2\Timer-1		
			If p2\Timer=0 
				Delete p2
				NofParticles2=NofParticles2-1
			EndIf
		EndIf
				
	Next
	
	
End Function

Function RenderParticles()

	; Should be called once every frame. Builds the mesh(es) and calculates the vertices
	; of each particle in existance

	pivot1=CreatePivot() ; used for pointing toward camera
	pivot2=CreatePivot()
	
	ParticleFixedCameraCheck()
	
	particlenumber=0 ; internal count for triangle creation
	nudge#=.001 ; push this much inward from texture border to avoid grabbing pieces of neighbour
	
	
	mySurface=ParticleSurface
	
	ClearSurface mySurface
			
	For p.particle = Each particle	; go through particles and render those associated with this mesh
	
		
		
		size#=p\size
		
		
		; 8x8 textures
		; pre-calculuate (u1,v1) and (u2,v2) corner values for speed increase
		tex=p\Texture
		u1#=Float(tex Mod 8)*0.125+nudge
		u2#=u1+0.125-2*nudge
		v1#=Float(Floor(tex/8))*0.125+nudge
		v2#=v1+0.125-2*nudge
		
		

		
		Select p\Orient
		
		Case 0
		
			; full position toward camera, + turning the sprite
			; this is done by creating a vector (pivot1) from the camera to the particle,
			; then a pivot at the origin (pivot2) is pointed in the direction of this vector
			; Then TForm is used on basis vectors to see how they would translate to the world
			; of the oriented pivot2. 
			; For speed purposes, two such TForms are performed. The first is (-1,1) upper left
			; corner of the particle (which is then negated for lower right), the
			; second is (1,1) upper right, which is then negated for lower left.
			; As a result, the vertex order is not my standard order.
			
			PositionEntity pivot1,p\x-EntityX(Camera),p\y-EntityY(Camera),p\z-EntityZ(Camera)
		
			PointEntity pivot2,pivot1
			TurnEntity pivot2,0,0,-p\angle
			
			TFormVector -size,size,0,pivot2,0
			AddVertex (mySurface,p\x+TFormedX(),p\y+TFormedY(),p\z+TFormedZ(),u1,v1)
			AddVertex (mySurface,p\x-TFormedX(),p\y-TFormedY(),p\z-TFormedZ(),u2,v2)
			TFormVector size,size,0,pivot2,0
			AddVertex (mySurface,p\x+TFormedX(),p\y+TFormedY(),p\z+TFormedZ(),u2,v1)
			AddVertex (mySurface,p\x-TFormedX(),p\y-TFormedY(),p\z-TFormedZ(),u1,v2)
			
		Case 1									
			; turning only left/right, i.e. no changes in y-coordinates.
			; this is useful if e.g. the camera's angle of depression from the horizon is low
			; and camera always looks in positive z-direction
			; (if needed, these could be further updated to do the other two orientations)
			
			; calculations are simply by linear transformation along the perpendicular of
			; the 2D "camera to particle" vector
			
			dx#=p\x-EntityX(Camera)
			dz#=p\z-EntityZ(Camera)
			
			rho#=Sqr((dx)^2+(dz)^2)
			dx#=dx/rho
			dz#=dz/rho

			AddVertex (mySurface,p\x-size*dz,p\y+size,p\z+size*dx,u1,v1)
			AddVertex (mySurface,p\x+size*dz,p\y-size,p\z-size*dx,u2,v2)
			AddVertex (mySurface,p\x+size*dz,p\y+size,p\z-size*dx,u2,v1)
			AddVertex (mySurface,p\x-size*dz,p\y-size,p\z+size*dx,u1,v2)

		Case 2
			; no turning at all
			; these particles simply "live" in the x/y plane
			; Very fast. And possibly useful for certain types of particles
			; and if particlemesh is a child of the camera
			AddVertex (mySurface,p\x-size,p\y+size,p\z,u1,v1)
			AddVertex (mySurface,p\x+size,p\y-size,p\z,u2,v2)
			AddVertex (mySurface,p\x+size,p\y+size,p\z,u2,v1)
			AddVertex (mySurface,p\x-size,p\y-size,p\z,u1,v2)
			

		Case 3
		
			; position toward camera, BUT NO SPRITE ROTATION
			; however this variation points all particles toward the camera as if
			; they were straight ahead of the camera... i.e. only one TForm calculation
			; is done at the start and used for all particles.
			
			; there seems to be little quality difference between this and method 0,
			; but much faster
			
			rotation=Floor(p\angle)
			If rotation <0
				Repeat
					rotation=rotation+360				
				Until rotation=>0
			EndIf
			If rotation >=360
				rotation=rotation Mod 360
			EndIf
			
			
			AddVertex (mySurface,p\x+SavedTFormedX1(rotation)*size,p\y+SavedTFormedY1(rotation)*size,p\z+SavedTFormedZ1(rotation)*size,u1,v1)
			AddVertex (mySurface,p\x-SavedTFormedX1(rotation)*size,p\y-SavedTFormedY1(rotation)*size,p\z-SavedTFormedZ1(rotation)*size,u2,v2)
			
			AddVertex (mySurface,p\x+SavedTFormedX2(rotation)*size,p\y+SavedTFormedY2(rotation)*size,p\z+SavedTFormedZ2(rotation)*size,u2,v1)
			AddVertex (mySurface,p\x-SavedTFormedX2(rotation)*size,p\y-SavedTFormedY2(rotation)*size,p\z-SavedTFormedZ2(rotation)*size,u1,v2)
		
		Case 4
			; no turning at all
			; these particles simply "live" in the x/z plane
			; Very fast. And possibly useful for certain types of particles
			; and if particlemesh is a child of the camera
			AddVertex (mySurface,p\x-size,p\y,p\z+size,u1,v1)
			AddVertex (mySurface,p\x+size,p\y,p\z-size,u2,v2)
			AddVertex (mySurface,p\x+size,p\y,p\z+size,u2,v1)
			AddVertex (mySurface,p\x-size,p\y,p\z-size,u1,v2)
			
		Case 5
			; flat on the ground
			; these particles simply "live" in the y/z plane
			; Very fast. And possibly useful for certain types of particles
			; and if particlemesh is a child of the camera
			AddVertex (mySurface,p\x,p\y-size,p\z+size,u1,v1)
			AddVertex (mySurface,p\x,p\y+size,p\z-size,u2,v2)
			AddVertex (mySurface,p\x,p\y+size,p\z+size,u2,v1)
			AddVertex (mySurface,p\x,p\y-size,p\z-size,u1,v2)


									
		End Select	
		
		
		; *** Note again that in order to lower the need for TForms in case zero, I've switched
		; the usual order of the quad vertices - upper/left, lower/right, then upper/right and lower/left
		AddTriangle (mySurface,particlenumber*4+0,particlenumber*4+2,particlenumber*4+3)
		AddTriangle (mySurface,particlenumber*4+2,particlenumber*4+1,particlenumber*4+3)
		
		If p\Lifespan>0
			alpha#=Float(p\Timer)/Float(p\Lifespan)
			For j=0 To 3
				VertexColor mySurface,particlenumber*4+j,255,255,255,alpha
			Next
		EndIf


		particlenumber=particlenumber+1
		
	Next
	
	
	particlenumber=0 ; internal count for triangle creation
	nudge#=.001 ; push this much inward from texture border to avoid grabbing pieces of neighbour
	
	
	mySurface=ParticleSurface2
	
	ClearSurface mySurface
			
	For p2.particle2 = Each particle2	; go through particles and render those associated with this mesh
	
		
		
		size#=p2\size
		
		
		; 8x8 textures
		; pre-calculuate (u1,v1) and (u2,v2) corner values for speed increase
		tex=p2\Texture
		u1#=Float(tex Mod 8)*0.125+nudge
		u2#=u1+0.125-2*nudge
		v1#=Float(Floor(tex/8))*0.125+nudge
		v2#=v1+0.125-2*nudge
		
		

		
		Select p2\Orient
		
		Case 0
		
			; full position toward camera, + turning the sprite
			; this is done by creating a vector (pivot1) from the camera to the particle,
			; then a pivot at the origin (pivot2) is pointed in the direction of this vector
			; Then TForm is used on basis vectors to see how they would translate to the world
			; of the oriented pivot2. 
			; For speed purposes, two such TForms are performed. The first is (-1,1) upper left
			; corner of the particle (which is then negated for lower right), the
			; second is (1,1) upper right, which is then negated for lower left.
			; As a result, the vertex order is not my standard order.
			
			PositionEntity pivot1,p2\x-EntityX(Camera),p2\y-EntityY(Camera),p2\z-EntityZ(Camera)
		
			PointEntity pivot2,pivot1
			TurnEntity pivot2,0,0,-p\angle
			
			TFormVector -size,size,0,pivot2,0
			AddVertex (mySurface,p2\x+TFormedX(),p2\y+TFormedY(),p2\z+TFormedZ(),u1,v1)
			AddVertex (mySurface,p2\x-TFormedX(),p2\y-TFormedY(),p2\z-TFormedZ(),u2,v2)
			TFormVector size,size,0,pivot2,0
			AddVertex (mySurface,p2\x+TFormedX(),p2\y+TFormedY(),p2\z+TFormedZ(),u2,v1)
			AddVertex (mySurface,p2\x-TFormedX(),p2\y-TFormedY(),p2\z-TFormedZ(),u1,v2)
			
		Case 1									
			; turning only left/right, i.e. no changes in y-coordinates.
			; this is useful if e.g. the camera's angle of depression from the horizon is low
			; and camera always looks in positive z-direction
			; (if needed, these could be further updated to do the other two orientations)
			
			; calculations are simply by linear transformation along the perpendicular of
			; the 2D "camera to particle" vector
			
			dx#=p2\x-EntityX(Camera)
			dz#=p2\z-EntityZ(Camera)
			
			rho#=Sqr((dx)^2+(dz)^2)
			dx#=dx/rho
			dz#=dz/rho

			AddVertex (mySurface,p2\x-size*dz,p2\y+size,p2\z+size*dx,u1,v1)
			AddVertex (mySurface,p2\x+size*dz,p2\y-size,p2\z-size*dx,u2,v2)
			AddVertex (mySurface,p2\x+size*dz,p2\y+size,p2\z-size*dx,u2,v1)
			AddVertex (mySurface,p2\x-size*dz,p2\y-size,p2\z+size*dx,u1,v2)

		Case 2
			; no turning at all
			; these particles simply "live" in the x/y plane
			; Very fast. And possibly useful for certain types of particles
			; and if particlemesh is a child of the camera
			AddVertex (mySurface,p2\x-size,p2\y+size,p2\z,u1,v1)
			AddVertex (mySurface,p2\x+size,p2\y-size,p2\z,u2,v2)
			AddVertex (mySurface,p2\x+size,p2\y+size,p2\z,u2,v1)
			AddVertex (mySurface,p2\x-size,p2\y-size,p2\z,u1,v2)
			

		Case 3
		
			; position toward camera, BUT NO SPRITE ROTATION
			; however this variation points all particles toward the camera as if
			; they were straight ahead of the camera... i.e. only one TForm calculation
			; is done at the start and used for all particles.
			
			; there seems to be little quality difference between this and method 0,
			; but much faster
			
			rotation=Floor(p2\angle)
			If rotation <0
				Repeat
					rotation=rotation+360				
				Until rotation=>0
			EndIf
			If rotation >=360
				rotation=rotation Mod 360
			EndIf
			
			
			AddVertex (mySurface,p2\x+SavedTFormedX1(rotation)*size,p2\y+SavedTFormedY1(rotation)*size,p2\z+SavedTFormedZ1(rotation)*size,u1,v1)
			AddVertex (mySurface,p2\x-SavedTFormedX1(rotation)*size,p2\y-SavedTFormedY1(rotation)*size,p2\z-SavedTFormedZ1(rotation)*size,u2,v2)
			
			AddVertex (mySurface,p2\x+SavedTFormedX2(rotation)*size,p2\y+SavedTFormedY2(rotation)*size,p2\z+SavedTFormedZ2(rotation)*size,u2,v1)
			AddVertex (mySurface,p2\x-SavedTFormedX2(rotation)*size,p2\y-SavedTFormedY2(rotation)*size,p2\z-SavedTFormedZ2(rotation)*size,u1,v2)
		
		Case 4
			; no turning at all
			; these particles simply "live" in the x/z plane
			; Very fast. And possibly useful for certain types of particles
			; and if particlemesh is a child of the camera
			AddVertex (mySurface,p2\x-size,p2\y,p2\z+size,u1,v1)
			AddVertex (mySurface,p2\x+size,p2\y,p2\z-size,u2,v2)
			AddVertex (mySurface,p2\x+size,p2\y,p2\z+size,u2,v1)
			AddVertex (mySurface,p2\x-size,p2\y,p2\z-size,u1,v2)
			
		Case 5
			; flat on the ground
			; these particles simply "live" in the y/z plane
			; Very fast. And possibly useful for certain types of particles
			; and if particlemesh is a child of the camera
			AddVertex (mySurface,p2\x,p2\y-size,p2\z+size,u1,v1)
			AddVertex (mySurface,p2\x,p2\y+size,p2\z-size,u2,v2)
			AddVertex (mySurface,p2\x,p2\y+size,p2\z+size,u2,v1)
			AddVertex (mySurface,p2\x,p2\y-size,p2\z-size,u1,v2)


									
		End Select	
		
		
		; *** Note again that in order to lower the need for TForms in case zero, I've switched
		; the usual order of the quad vertices - upper/left, lower/right, then upper/right and lower/left
		AddTriangle (mySurface,particlenumber*4+0,particlenumber*4+2,particlenumber*4+3)
		AddTriangle (mySurface,particlenumber*4+2,particlenumber*4+1,particlenumber*4+3)
		
		If p2\Lifespan>0
			alpha#=Float(p2\Timer)/Float(p2\Lifespan)
			For j=0 To 3
				VertexColor mySurface,particlenumber*4+j,255,255,255,alpha
			Next
		EndIf


		particlenumber=particlenumber+1
		
	Next

	
	FreeEntity pivot1
	FreeEntity pivot2
	
	
End Function

Function ParticleFixedCameraCheck()

	; Here we check if the precalulated rotated transforms (0-359 of them)
	; need to be re-done, i.e. if the camera angle has changed
	
	; The System pre-calculated transformations (for orient-mode=3, which really should be the default)
	; based on the current camera orientation, and 360 rotations of particles (more detail can be easily
	; implemented, but should hardly be necessary)
	; If the camera angle changes, then these 360 sets are re-calculated
	
	If EntityPitch(Camera)<>SavedTFormedPitch Or EntityYaw(Camera)<>SavedTFormedYaw Or EntityRoll(Camera)<>SavedTFormedRoll
		
		Pivot=CreatePivot()
		SavedTFormedPitch=EntityPitch(Camera)
		SavedTFormedYaw=EntityYaw(Camera)
		SavedTFormedRoll=EntityRoll(Camera)

		RotateEntity Pivot,SavedTFormedPitch,SavedTFormedYaw,SavedTFormedRoll,True
		
		For i=0 To 359
			
			TFormVector -.5,.5,0,pivot,0
			SavedTFormedX1(i)=TFormedX()
			SavedTFormedY1(i)=TFormedY()
			SavedTFormedZ1(i)=TFormedZ()
			TFormVector .5,.5,0,pivot,0
			SavedTFormedX2(i)=TFormedX()
			SavedTFormedY2(i)=TFormedY()
			SavedTFormedZ2(i)=TFormedZ()
			TurnEntity Pivot,0,0,-1
		Next
	
	
		FreeEntity Pivot
	EndIf

End Function

Function ResetText(texfile$)

	; Reset all ParticleMeshes and Clears all particles, then creates new mesh with texture
	;
	; Could be called once e.g. every level startup, or at the end to cleanup

		
	If TextTexture<>0
		FreeTexture TextTexture
		TextTexture=0
	EndIf
	If TextMesh<>0
		FreeEntity TextMesh
		TextMesh=0
	EndIf
	
	NofLetters=0
	
	For p.Letter = Each Letter
		Delete p
	Next
	
	TextMesh=CreateMesh(camera)
	
	TextSurface=CreateSurface(TextMesh)
	EntityFX TextMesh,35 ; use 32+1+2=35 for alpha
		
	EntityOrder TextMesh,-20

	
	TextTexture=myLoadTexture(texfile$,4)
	EntityTexture TextMesh,TextTexture
	
End Function

Function AddLetter(tex,x#,y#,z#,angle#,size#,dx#,dy#,dz#,dangle#,dsize#,ddx#,ddy#,ddz#,LifeSpan,red,green,blue)

	;Texture	; what cell # in the texture (e.g. 0-63 in an 8x8)
	;x#,y#,z#	; x/y/z position of center
	;angle#		; angle (0-359 degrees), rotated clockwise
	;size#		; size in units (i.e. diameter)
	;dx#,dy#,dz#; dx/dy/dz velocity 
	;dangle#	; rotation speed (in degrees, clockwise)
	;dsize#	; change in size
	;ddx,ddy,ddz; ddx/ddy/ddz acceleration
	;LifeSpan	; how many cycles does it stick around. Zero = infinite life
	;Orient		; 0- point to camera, 1- vary only horizontally toward camera, 2- no change (stay in x/y plane)


	p.Letter=New Letter
	p\Texture=tex
	p\x=x
	p\y=y
	p\z=z
	p\angle=angle
	p\size=size/2.0	; radius, hence divide "diameter" by 2 
	p\dx=dx	
	p\dy=dy
	p\dz=dz
	p\dangle=dangle
	p\dsize=dsize
	p\ddx=ddx
	p\ddy=ddy
	p\ddz=ddz
	p\LifeSpan=LifeSpan
	p\Timer=LifeSpan ; internal use - clocks down until zero
	p\Red=red
	p\Green=green
	p\Blue=blue
		
	p\Change=0 ; internal use - decide in advance what variables will need updating to speed up
	If p\dx<>0 Or p\dy<>0 Or p\dz<>0
		p\Change=1
		If p\dangle<>0 Or p\dsize<>0
			p\Change=p\Change+2
		EndIf
	EndIf
	If p\ddx<>0 Or p\ddy<>0 Or p\ddz<>0
		p\Change=2
		If p\dangle<>0 Or p\dsize<>0
			p\Change=p\Change+2
		EndIf
	EndIf
	If p\Change=0 And (p\dangle<>0 Or p\dsize<>0)
		p\Change=5
	EndIf

End Function


Function ControlLetters()

	; deals with particles once set in motion (no individual control possible)
	; should be called once every cycle
	
	For p.particle = Each particle
				
		Select p\Change ; pre-determined what needs to be updated
		Case 0:
			; no action (other than fading out)
		Case 1:	
			; Bare-Bones straight movement
			; no rotate/resize/accelerate
			
			; smoke, fire, etc.
			
			p\x=p\x+p\dx
			p\y=p\y+p\dy
			p\z=p\z+p\dz
		
		Case 2:
			; parabolic movement
			; change velocity (acceleration)
			p\dx=p\dx+p\ddx
			p\dy=p\dy+p\ddy
			p\dz=p\dz+p\ddz
			p\x=p\x+p\dx
			p\y=p\y+p\dy
			p\z=p\z+p\dz
			
		Case 3:	
			; straight movement
			; with rotation
			p\x=p\x+p\dx
			p\y=p\y+p\dy
			p\z=p\z+p\dz
			p\angle=p\angle+p\dangle
			p\size=p\size+p\dsize
		
		Case 4:
			; parabolic movement
			; change velocity (acceleration), with rotation
			p\dx=p\dx+p\ddx
			p\dy=p\dy+p\ddy
			p\dz=p\dz+p\ddz
			p\x=p\x+p\dx
			p\y=p\y+p\dy
			p\z=p\z+p\dz
			p\angle=p\angle+p\dangle
			p\size=p\size+p\dsize
		Case 5:	
			; stationary but rotating/resizing
			p\angle=p\angle+p\dangle
			p\size=p\size+p\dsize

		End Select
			
		;phase out and lifetime 
		If p\LifeSpan>0		
			p\Timer=p\Timer-1		
			If p\Timer=0 
				Delete p
				NofParticles=NofParticles-1
			EndIf
		EndIf
				
	Next
	
	
End Function

Function RenderLetters()

	letternumber=0 ; internal count for triangle creation
	nudge#=.004 ; push this much inward from texture border to avoid grabbing pieces of neighbour
	
	
	mySurface=TextSurface
	
	ClearSurface mySurface
			
	For p.letter = Each letter	; go through particles and render those associated with this mesh
	
		size#=p\size
		
		; 8x8 textures
		; pre-calculuate (u1,v1) and (u2,v2) corner values for speed increase
		tex=p\Texture
		u1#=Float(tex Mod 16)*0.0625+nudge
		u2#=u1+0.0625-2*nudge
		v1#=Float(Floor(tex/16))*0.125+nudge
		v2#=v1+0.125-2*nudge
			
		; no turning at all
		; these particles simply "live" in the x/y plane
		; Very fast. And possibly useful for certain types of particles
		; and if particlemesh is a child of the camera
		AddVertex (mySurface,p\x-size,p\y+2*size,p\z,u1,v1)
		AddVertex (mySurface,p\x+size,p\y-2*size,p\z,u2,v2)
		AddVertex (mySurface,p\x+size,p\y+2*size,p\z,u2,v1)
		AddVertex (mySurface,p\x-size,p\y-2*size,p\z,u1,v2)
	
						
		; *** Note again that in order to lower the need for TForms in case zero, I've switched
		; the usual order of the quad vertices - upper/left, lower/right, then upper/right and lower/left
		AddTriangle (mySurface,letternumber*4+0,letternumber*4+2,letternumber*4+3)
		AddTriangle (mySurface,letternumber*4+2,letternumber*4+1,letternumber*4+3)
		
		If p\Lifespan>0
			alpha#=Float(p\Timer)/Float(p\Lifespan)
		Else 
			alpha#=1.0	
		EndIf
		For j=0 To 3
			VertexColor mySurface,letternumber*4+j,p\red,p\green,p\blue,alpha
		Next

		letternumber=letternumber+1
		
		Delete p
		
	Next
	
End Function

Function DisplayText(mytext$,x#,y#,size#,spacing#,red,green,blue)
	
	For i=1 To Len(mytext$)
	
		let=Asc(Mid$(mytext$,i,1))-32
		If widescreen
			AddLetter(let,(-.97+(x+i-1)*.0396*size*spacing)/CameraZoomLevel,(.7-y*.0623*size*spacing)/CameraZoomLevel,1.33,0,.044*size/CameraZoomLevel,0,0,0,0,0,0,0,0,1,red,green,blue)
		Else
			AddLetter(let,(-.97+(x+i-1)*.0396*size*spacing)/CameraZoomLevel,(.7-y*.0623*size*spacing)/CameraZoomLevel,1.0,0,.044*size/CameraZoomLevel,0,0,0,0,0,0,0,0,1,red,green,blue)
		EndIf
		
	Next
End Function