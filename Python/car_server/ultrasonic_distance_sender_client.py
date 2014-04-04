import socket
import sys
import time
import RPi.GPIO as GPIO

HOST, PORT = "localhost", 80

# Create a socket (SOCK_STREAM means a TCP socket)
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect to server and send data
sock.connect((HOST, PORT))
###########GPIO WORK###############
# -----------------------
# Define some functions
# -----------------------

def measure():
	# This function measures a distance

	GPIO.output(GPIO_TRIGGER, True)
	time.sleep(0.00001)
	GPIO.output(GPIO_TRIGGER, False)
	start = time.time()

	while GPIO.input(GPIO_ECHO)==0:
		start = time.time()

	while GPIO.input(GPIO_ECHO)==1:
		stop = time.time()

	elapsed = stop-start
	distance = (elapsed * 34300)/2

	return distance

def measure_average():
	# This function takes 3 measurements and
	# returns the average.

	distance1=measure()
	time.sleep(0.1)
	distance2=measure()
	time.sleep(0.1)
	distance3=measure()
	distance = distance1 + distance2 + distance3
	distance = distance / 3
	return distance

# -----------------------
# Main Script
# -----------------------

# Use BCM GPIO references
# instead of physical pin numbers
GPIO.setmode(GPIO.BCM)

# Define GPIO to use on Pi
GPIO_TRIGGER = 23
GPIO_ECHO    = 24

print "Ultrasonic Measurement"

# Set pins as output and input
GPIO.setup(GPIO_TRIGGER,GPIO.OUT)  # Trigger
GPIO.setup(GPIO_ECHO,GPIO.IN)      # Echo

# Set trigger to False (Low)
GPIO.output(GPIO_TRIGGER, False)

# Wrap main content in a try block so we can
# catch the user pressing CTRL-C and run the
# GPIO cleanup function. This will also prevent
# the user seeing lots of unnecessary error
# messages.
try:
	sock.send("iam:ultrasonicsensor")
	while True:

		distance = measure_average()
		float_string = "{:.1f}".format(distance)
		full_string = "msg:" + float_string
		print "Distance : %.1f" % distance
		sock.send(full_string)
		time.sleep(1)
except KeyboardInterrupt:
	# User pressed CTRL-C
	# Reset GPIO settings
	GPIO.cleanup()
	sock.close()
