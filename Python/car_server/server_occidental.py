from twisted.internet.protocol import Protocol, Factory
from twisted.internet import reactor
import RPi.GPIO as io

io.setmode(io.BCM)

left_in1_pin = 23
left_in2_pin = 24

right_in1_pin = 4
right_in2_pin = 25

left_ind_pin = 22
right_ind_pin = 27

class Motor(object):
	def __init__(self, in1_pin, in2_pin,left_ind_pin,right_ind_pin):
		self.in1_pin = in1_pin
		self.in2_pin = in2_pin

		self.left_ind_pin = left_ind_pin
		self.right_ind_pin = right_ind_pin

		
		io.setup(self.in1_pin, io.OUT)
		io.setup(self.in2_pin, io.OUT)

		io.setup(self.left_ind_pin, io.OUT)
		io.setup(self.right_ind_pin, io.OUT)

		
	def clockwise(self):
		io.output(self.in1_pin, True)    
		io.output(self.in2_pin, False)

	def counter_clockwise(self):
		io.output(self.in1_pin, False)
		io.output(self.in2_pin, True)

	def left_indicator(self):
		io.output(self.right_ind_pin, False)
		io.output(self.left_ind_pin, True)

	def right_indicator(self):
		io.output(self.left_ind_pin, False)
		io.output(self.right_ind_pin, True)
		
	def stop(self):
		io.output(self.in1_pin, False)    
		io.output(self.in2_pin, False)
		io.output(self.left_ind_pin, False)
		io.output(self.right_ind_pin, False)

		
def set(property, value):
    try:
        f = open("/sys/class/rpi-pwm/pwm0/" + property, 'w')
        f.write(value)
        f.close()	
    except:
        print("Error writing to: " + property + " value: " + value)
        
try:
	set("delayed", "0")
	set("frequency", "500")
	set("active", "1")

	left_motor = Motor(left_in1_pin, left_in2_pin,left_ind_pin,right_ind_pin)
	right_motor = Motor(right_in1_pin, right_in2_pin,left_ind_pin,right_ind_pin)
	
except KeyboardInterrupt:
#	left_motor.stop()
#	right_motor.stop()
	print "\nstopped"
    




class IphoneChat(Protocol):

	def connectionMade(self):
		#self.transport.write("""connected""")
		self.factory.clients.append(self)
		print "clients are ", self.factory.clients
	
	def connectionLost(self, reason):
	    self.factory.clients.remove(self)
	
	def dataReceived(self, data):
	    #print "data is ", data
		a = data.split(':')
		if len(a) > 1:
			command = a[0]
			content = a[1]
			
			msg = ""
			if command == "iam":
				self.name = content
				msg = self.name + " has joined"
				
			elif command == "msg":
				msg = content
#				msg = self.name + ": " + content
				b = msg.split(',')
				if any("Tag = 1" in s for s in b):
					print "tag matched"
					if " cmd = up" in b:
						print "up matched"
						left_motor.stop()
					elif " cmd = down" in b:
						left_motor.clockwise()
						print "down matched"

				if any("Tag = 2" in s for s in b):
					print "tag matched"
					if " cmd = up" in b:
						print "up matched"
						left_motor.stop()
					elif " cmd = down" in b:
						left_motor.counter_clockwise()
						print "down matched"

				if any("Tag = 3" in s for s in b):
					print "tag matched"
					if " cmd = up" in b:
						print "up matched"
						right_motor.stop()
					elif " cmd = down" in b:
						right_motor.clockwise()
						print "down matched"

				if any("Tag = 4" in s for s in b):
					print "tag matched"
					if " cmd = up" in b:
						print "up matched"
						right_motor.stop()
					elif " cmd = down" in b:
						right_motor.counter_clockwise()
						print "down matched"
						
			print msg
						
			for c in self.factory.clients:
				c.message(msg)
				
	def message(self, message):
		self.transport.write(message + '\n')


		
factory = Factory()
factory.protocol = IphoneChat
factory.clients = []

reactor.listenTCP(80, factory)
print "RC Controller started"
reactor.run()

#left_motor.stop()
#left_motor.stop()

