from dronekit import connect, VehicleMode

#print("Connecting to drone on /dev/ttyAMA0")
vehicle = connect('/dev/ttyAMA0', wait_ready=False)

print (vehicle.gps_0)
print (vehicle.battery)
print (vehicle.last_heartbeat)
print (vehicle.is_armable)
print (vehicle.armed)
print (vehicle.system_status.state)
print (vehicle.mode.name)
print (vehicle.groundspeed)
print (vehicle.heading)

vehicle.close()

sitl.stop()
#print("Completed")