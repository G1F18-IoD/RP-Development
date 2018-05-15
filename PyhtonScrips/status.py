from dronekit import connect, VehicleMode

print("Connecting to drone on /dev/ttyAMA0")
vehicle = connect('/dev/ttyAMA0', wait_ready=False)

print ("GPS: %s" % vehicle.gps_0)
print (" Battery: %s" % vehicle.battery)
print (" Last Heartbeat: %s" % vehicle.last_heartbeat)
print (" Is Armable?: %s" % vehicle.is_armable)
print (" System status: %s" % vehicle.system_status.state)
print (" Mode: %s" % vehicle.mode.name)

vehicle.close()

sitl.stop()
print("Completed")