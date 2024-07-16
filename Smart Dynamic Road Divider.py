import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522
import threading
import signal
import time

GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)
exit_event = threading.Event()

#FUNCTION TO ROTATE THE MOTORS TOWARDS RIGHT

def rotate_right():
	print('Rotating in right direction')
	GPIO.output(5, GPIO.HIGH)
	GPIO.output(7, GPIO.LOW)
	pwm.ChangeDutyCycle(80)
	time.sleep(5)
	pwm.ChangeDutyCycle(0)

#FUNCTION TO ROTATE THE MOTORS TOWARDS RIGHT	
def rotate_left():
	print('Rotating in left direction')	
	GPIO.output(5, GPIO.LOW)
	GPIO.output(7, GPIO.HIGH)
	pwm.ChangeDutyCycle(80)
	time.sleep(5)
	pwm.ChangeDutyCycle(0)

#FUNCTION TO COUNT TRAFFIC DENSITY
def traffic_count(traffic_density):
	moved = 0

	while True:
		if exit_event.is_set():
			return
		
		val1 = GPIO.input(31)
		val2 = GPIO.input(29)
		print(val1,"\t", val2)

		if val1 == 0 :
			traffic_density+=1	
		if val2 == 0:
			traffic_density-=1
	
		print("Traffic Density: ",traffic_density)	
		
		if traffic_density >= 5 :
			GPIO.output(11, False)
			GPIO.output(13, True)
			
			if moved == 0:
				rotate_right()
				moved+=1
			
		time.sleep(1)
		

#FUNCTION TO CHECK IF AMBULANCE HAS PASSED		
def ambulance_passing():
	id , vehicle = reader.read()
	print(vehicle.rstrip()," has passed")

	if id == 358863993553:
		rotate_left()
		exit_event.set()
		return
	elif id == 152996273448:
		print("TRAFFIC SIGNAL VIOLATED")
		ambulance_passing()
	

try:
	GPIO.setup(29, GPIO.IN)			#IR SENSOR-1 (USED TO INC)
	GPIO.setup(31, GPIO.IN)			#IR SENSOR-2 (USED TO DEC)
	
	GPIO.setup(11, GPIO.OUT)		#RED LED 
	GPIO.setup(13, GPIO.OUT)		#GREEN LED 
	
	GPIO.setup(3, GPIO.OUT)			#ENABLE OF L298N MOTOR DRIVER 
	GPIO.setup(5, GPIO.OUT)			#INPUT-1 OF L298N MOTOR DRIVER 
	GPIO.setup(7, GPIO.OUT)			#INPUT-2 OF L298N MOTOR DRIVER 
	pwm.start(0)
	
	reader = SimpleMFRC522()		#TO READ RFID READER

	traffic_density = 0
	timer = 0
	t1 = threading.Thread(target = traffic_count, args = [traffic_density])
	t2 = threading.Thread(target = ambulance_passing)

	
	while True:
		GPIO.output(11, True)
		GPIO.output(13, False)
		
		ambulance = input("Is an Ambulance approaching? [Y/N]")
		
		if ambulance == 'y' or ambulance == 'Y' :
			t1.start()
			t2.start()
			t1.join()
			t2.join()
		time.sleep(5)
			
finally:
	GPIO.cleanup()

