import dronekit_sitl
sitl = dronekit_sitl.start_default()
connection_string = sitl.connection_string()

from dronekit import connect, VehicleMode

print("Connecting to drone on %s" % (connection_string,))
vehicle = connect(connection_string, wait_ready=True)

print ("GPS: %s" % vehicle.gps_0)
print (" Battery: %s" % vehicle.battery)
print (" Last Heartbeat: %s" % vehicle.last_heartbeat)
print (" Is Armable?: %s" % vehicle.is_armable)
print (" System status: %s" % vehicle.system_status.state)
print (" Mode: %s" % vehicle.mode.name)

vehicle.close()

sitl.stop()
print("Completed")