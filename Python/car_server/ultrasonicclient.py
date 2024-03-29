import socket
import time
import RPi.GPIO as GPIO


client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(('localhost', 80))


#############GPIO Work################
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

  while True:
    distance = measure_average()
    print "Distance : %.1f" % distance
	# Receive data from the server and shut down
	received = client_socket.recv(1024)
	a = received.split(':')
	if len(a) > 1:
		command = a[0]
		content = a[1]
		msg = ""
		if command == "msg" and content == "q":
        client_socket.close()
        break
	else:
		client_socket.send("msg:"+distance)	
	#print "Sent:     %s" % data
	#print "Received: %s" % received
	time.sleep(1)
except KeyboardInterrupt:
	# User pressed CTRL-C
	# Reset GPIO settings
	GPIO.cleanup()
###########Clean All GPIO if program ends and close the socket ###########
GPIO.cleanup()
client_socket.close()
