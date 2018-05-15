#x = north/south
#y = east/west
#z = up/down
#t = duration in seconds

NORTH = 2
SOUTH = -2
EAST = 2
WEST = -2
UP = -0.5
DOWN = 0.5
SPEED = 5
t = 5

#use defined values in place of function parameters
vehicle.groundspeed(SPEED)
send_ned_velocity(x,y,z,t)
time.sleep(DURATION)