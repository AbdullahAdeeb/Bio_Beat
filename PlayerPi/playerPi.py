<<<<<<< HEAD
# importing important directories to ensure proper functioning 
=======
##############################
####			  ####
#### Class Player Pi	  ####
#### Primary Contributor: ####
#### Tejas Patil 100823713####
####			  ####
##############################

# importing important directories 
>>>>>>> f35d1112730f5ba881bd23c395a8c11683f987da

import socket
import thread
import pygame
import binascii
import pygame
import sys

# react to the command sent by the GUI
def commandReceived(object):
	
	# byte array object to separate the byte received using UDP into separate bytes elements to process the options
	x = bytearray(object)
	x1 = x[:1]
	x2 = x[1:2]
	x3 = x[2:3]
	x4 = x[3:4]
	x5 = x[4:5]

	# interpret commands received from the GUI
	if(binascii.hexlify(x4) == b'01'):
		
		# the previous song will continue playback if play is pressed after pause
		if (isPaused == 1):
			pause()
		# the play feature will be accessed 
		else:
			play()
<<<<<<< HEAD

	elif(binascii.hexlify(x4) == b'02'):
		pause()
	elif(binascii.hexlify(x4) == b'03'):
		previous()
	elif(binascii.hexlify(x4) == b'04'):
		next()
	elif(binascii.hexlify(x4) == b'05'):
		print 'Command does not exist'
	elif(binascii.hexlify(x4) == b'06'):
		volinc()
	elif(binascii.hexlify(x4) == b'07'):
		voldec()
	elif(binascii.hexlify(x4) == b'08'):
		stop()
	else:
		print "wrong command"
=======
			
	# if the user selects pause the song will temporarily hault playback
	elif(binascii.hexlify(x4) == b'02'):
		pause()
	
	elif(binascii.hexlify(x4) == b'03'):
		previous()
		
	elif(binascii.hexlify(x4) == b'04'):
		next()
		
	elif(binascii.hexlify(x4) == b'05'):
		print 'Command does not exist'
		
	# user selects the increase volume option
	elif(binascii.hexlify(x4) == b'06'):
		volinc()
		
	# user selects the decrease volume option
	elif(binascii.hexlify(x4) == b'07'):
		voldec()
	
	# stop the playback of the song
	elif(binascii.hexlify(x4) == b'08'):
		stop()
	else:
		print "Invalid command"
>>>>>>> f35d1112730f5ba881bd23c395a8c11683f987da
#	else if(binascii.hexlify(x4) == b'09'):
#		mood(x5)

# Receive data containing user commands as a UDP message 
def receive():
	
	sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	sock.bind((UDP_MyIP, UDP_Port))
	while True:
		data, addr = sock.recvfrom(5)
		commandReceived(data)
		
	return 0;

# play the song
def play():
<<<<<<< HEAD

	if(isPaused == 0):	
		song = xmlParser(xmlDirectory)
		print song
		pygame.mixer.music.load(song)
		pygame.mixer.music.play(0)
	else:
=======
	
	# if the song is paused then parse the XML to find a song which can be played
	if(isPaused == 0):	
		song = xmlParser(xmlDirectory)
		print "Current song playing" + song
		pygame.mixer.music.load(song)
		pygame.mixer.music.play(0)
	else:
	
	# else pause the song 
>>>>>>> f35d1112730f5ba881bd23c395a8c11683f987da
		print "Paused in Play"
		pause()
#		sys.exit(0)

# pause the song and then continue playback by clicking play
def pause():

	global isPaused
<<<<<<< HEAD
=======
	
	# if the song is not paused then pause it 
>>>>>>> f35d1112730f5ba881bd23c395a8c11683f987da
	if (isPaused == 0):
		pygame.mixer.music.pause()
		#songPosition = pygame.mixer.music.get_pos()
		isPaused = 1
		print "Paused"
	#	sys.exit(0)

<<<<<<< HEAD
=======
	# unpause the song that has been temperarily paused
>>>>>>> f35d1112730f5ba881bd23c395a8c11683f987da
	else: 
		pygame.mixer.music.unpause()
		isPaused = 0
		print "Unpaused"
#		sys.exit(0)


def previous():
#	global prev
#	pygame.mixer.music.load(prev)

	print 'Command does not exist'

def next():
#	play()
	print 'Command does not exist'

#def generateQueue():

# increase the volume 
def volinc():
	pygame.mixer.music.set_volume(pygame.mixer.music.get_volume() + 0.1)

# decrease the volume 
def voldec():
	pygame.mixer.music.set_volume(pygame.mixer.music.get_volume() - 0.1)

# parse the XML director containing songs names and moods
def xmlParser(xmlDirectory):
	global prev
	from xml.dom import minidom
	
	xmldocument = minidom.parse(xmlDirectory)
	songsList = xmldocument.getElementsByTagName("song")
	
	for song in songsList:
		print songsList.length
		if ((song.attributes["play"].value) == "true" and song.firstChild.data != prev):
			prev = song.firstChild.data
			path = song.firstChild.data
			print "path is" ,  path
			return path

def stop():
	pygame.mixer.music.stop()

#def mood(data):
#	if(playStatus == False):
#		
#		if(binascii.hexlify(data) == b'01'):
#			#relaxed
#		elif(binascii.hexlify(data) == b'02'):
#			#excited
#		elif(binascii.hexlify(data) == b'03'):
#			#sad
#		elif(binascii.hexlify(data) == b'04'):
#			#happy
#		elif(binascii.hexlify(data) == b'05'):
#			#angry
#	
#		playStatus = True
	

# IP address to receive requests 
<<<<<<< HEAD
UDP_MyIP = "10.0.0.43"
=======
UDP_MyIP = "127.0.0.1"
>>>>>>> f35d1112730f5ba881bd23c395a8c11683f987da

# Port for UDP communication
UDP_Port = 5000

# initilizes the pygame settings for music playback
pygame.init()

# setting up the the GUI for pygame 
try:
	pygame.display.set_mode((200,100))
except KeyboardInterrupt:
	print "Raise"
	raise
except:
	# report error and proceed further steps
	print "Error occurred: Attempted display"

#initialize the volume for pygame
pygame.mixer.music.set_volume(0.5)

#shared memory
 
xmlDirectory = "/home/pi/public/BioBeat_songs.xml"
songPosition = 0.000

isPaused = 0
prev = "null"
receive()
