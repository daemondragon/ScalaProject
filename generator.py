import argparse
import random
import time
import json
import csv

parser = argparse.ArgumentParser(description='Generate scala file (.json and .csv)')
parser.add_argument('--directory', help='The directory where the file will be generated', default=".")
parser.add_argument('--seed', help='The seed used to generate the files (reproducible build)', default=int(time.time()))
parser.add_argument('--nb-files', type=int, help='The number of files to generate', default=20)
parser.add_argument('--nb-lines', type=int, help='The number of object per files', default=100)

args = parser.parse_args()
print(args)
random.seed(args.seed)

def normal(drone):
    drone["time"] += random.randint(-1, 10)

    if drone["battery"] > 0:#Can move
        drone["latitude"] += random.uniform(0.0001, 0.0001)
        drone["longitude"] += random.uniform(0.0001, 0.0001)

        #The battery drain faster the higher the temperature
        drone["battery"] = max(0, drone["battery"] - random.uniform(0, 3) * ((100 - drone["temperature"]) / 100))
        drone["temperature"] += random.uniform(-0.02, 0.02)

        drone["defect"] = int(random.randint(0, 100) <= 5)# 5% defect.
    else:
        drone["temperature"] += random.uniform(-0.1, 0)#Cooling down

def defects(drone):
    normal(drone)
    drone["defect"] = int(random.randint(0, 100) <= 92)# 92% defect.

def thief(drone):
    normal(drone)
    if drone["battery"] <= 0:#Can't move
        #Still moving
        drone["latitude"] += random.uniform(0.0001, 0.0001)
        drone["longitude"] += random.uniform(0.0001, 0.0001)

def fire(drone):
    normal(drone)
    if drone["temperature"] > 30:
        #Catching fire
        drone["temperature"] += random.uniform(0, 2.4)

def choose_scenario():
    scenario_sum = 0
    for (_, value, _) in scenarios:
        scenario_sum += value

    #Choice made
    choice = random.randint(0, scenario_sum)

    scenario_sum = 0

    for (name, value, function) in scenarios:
        scenario_sum += value
        if choice <= scenario_sum:
            return (name, function)
    return None

scenarios = [
    ("normal", 50 , normal),#No problem
    ("defect", 10, defects),#A lots of defects.
    ("thief", 1, thief),#The drone have been stoled
    ("fire", 5, fire),#The drone have caught fire
]


for file_index in range(args.nb_files):
    extension = ["json", "csv"][random.randint(0, 1)]
    (scenario_name, scenario) = choose_scenario()

    print("Generating file:", file_index, "with scenario:", scenario_name, extension)

    drones = [{
        "id" : random.randint(0, 60000),
        "latitude": random.uniform(-90, 90),
        "longitude": random.uniform(-180, 180),#Random position on earth
        "temperature": random.uniform(10, 30),#Normal temperature on start
        "battery": 100,#Full battery
        "defect": int(False),
        "time": random.randint(1551711535, 1638629935)
    }]

    for i in range(args.nb_lines - 1):
        drone = {
            "id": drones[i]["id"],
            "latitude": drones[i]["latitude"],
            "longitude": drones[i]["longitude"],
            "temperature": drones[i]["temperature"],
            "battery": drones[i]["battery"],
            "defect": drones[i]["defect"],
            "time": drones[i]["time"]
        }

        scenario(drone)
        drones.append(drone)

    with open("{0}/{1}_{2}.{3}".format(args.directory, '{:04d}'.format(file_index), scenario_name, extension), "w") as file:
        if extension == "csv":
            writer = csv.DictWriter(file, fieldnames=drones[0].keys())
            writer.writeheader()
            [writer.writerow(drone) for drone in drones]
        else:
            [file.write(json.dumps(drone) + "\n") for drone in drones]

