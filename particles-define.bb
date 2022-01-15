; ********************************************
;
; Single Surface Particle System Definitions
;
; ********************************************

; Two types of particles: 1- for in game coordinates, 2- bound to camera

Global ParticleMesh, ParticleSurface, ParticleMesh2, ParticleSurface2, ParticleTexture

Type Particle
	Field Texture	; what cell # in the texture (e.g. 0-63 in an 8x8)
	Field x#		; x/y/z position of center
	Field y#
	Field z#
	Field angle#	; angle (0-359 degrees), rotated clockwise
	Field size#		; size in units (i.e. diameter)
	Field dx#		; dx/dy/dz velocity 
	Field dy#
	Field dz#
	Field dangle#	; rotation speed (in degrees, clockwise)
	Field dsize#	; change in size
	Field ddx#		; ddx/ddy/ddz acceleration
	Field ddy#
	Field ddz#
	Field LifeSpan	; how many cycles does it stick around. Zero = infinite life
	Field Timer		; generated from Lifespan: counts down to zero
	Field Change	; internal use - optimize what things need to be change in udpate
	Field Orient	; 0- point particle to camera, 
					; 1- vary only horizontally toward camera, 
					; 2- no change (simply in x/y plane), 
					; 3- point in direction of camera, but using a single transformation 
					;	based on the straight forward direction of the particle (i.e. all particles
					;	will point the same way)
					
End Type

Global NofParticles=0

Type Particle2
	Field Texture	; what cell # in the texture (e.g. 0-63 in an 8x8)
	Field x#		; x/y/z position of center
	Field y#
	Field z#
	Field angle#	; angle (0-359 degrees), rotated clockwise
	Field size#		; size in units (i.e. diameter)
	Field dx#		; dx/dy/dz velocity 
	Field dy#
	Field dz#
	Field dangle#	; rotation speed (in degrees, clockwise)
	Field dsize#	; change in size
	Field ddx#		; ddx/ddy/ddz acceleration
	Field ddy#
	Field ddz#
	Field LifeSpan	; how many cycles does it stick around. Zero = infinite life
	Field Timer		; generated from Lifespan: counts down to zero
	Field Change	; internal use - optimize what things need to be change in udpate
	Field Orient	; 0- point particle to camera, 
					; 1- vary only horizontally toward camera, 
					; 2- no change (simply in x/y plane), 
					; 3- point in direction of camera, but using a single transformation 
					;	based on the straight forward direction of the particle (i.e. all particles
					;	will point the same way)
					
End Type

Global NofParticles2=0


; The System pre-calculated transformations (for orient-mode=3, which really should be the default)
; based on the current camera orientation, and 360 rotations of particles (more detail can be easily
; implemented, but should hardly be necessary)
; If the camera angle changes, then these 360 sets are re-calculated

Dim SavedTFormedX1#(360),SavedTFormedY1#(360),SavedTFormedZ1#(360)
Dim SavedTFormedX2#(360),SavedTFormedY2#(360),SavedTFormedZ2#(360)

Global SavedTFormedPitch#,SavedTFormedYaw#,SavedTFormedRoll#

; ***************************
; Text System
; ***************************

; Text is a separate single surface. 

Global TextMesh, TextSurface, TextTexture

Type Letter
	Field Texture	; what cell # in the texture (e.g. 0-63 in an 8x8)
	Field x#		; x/y/z position of center
	Field y#
	Field z#
	Field angle#	; angle (0-359 degrees), rotated clockwise
	Field size#		; size in units (i.e. diameter)
	Field dx#		; dx/dy/dz velocity 
	Field dy#
	Field dz#
	Field dangle#	; rotation speed (in degrees, clockwise)
	Field dsize#	; change in size
	Field ddx#		; ddx/ddy/ddz acceleration
	Field ddy#
	Field ddz#
	Field LifeSpan	; how many cycles does it stick around. Zero = infinite life
	Field Timer		; generated from Lifespan: counts down to zero
	Field Change	; internal use - optimize what things need to be change in udpate
	Field Red
	Field Green
	Field Blue
						
End Type