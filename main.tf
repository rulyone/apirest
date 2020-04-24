// Configure the Google Cloud provider
// CREDENTIALS_FILE.json is outside the project folder for security reasons.
// how to obtain this (getting project credentials): https://cloud.google.com/community/tutorials/getting-started-on-gcp-with-terraform
provider "google" {
  credentials = file("CREDENTIALS_FILE.json")
  project     = "sage-inn-274818"
  region      = "us-west1"
}

// Terraform plugin for creating random ids
resource "random_id" "instance_id" {
 byte_length = 8
}

resource "google_compute_address" "static" {
  name = "ipv4-address"
}

// A single Google Cloud Engine instance
resource "google_compute_instance" "default" {
 name         = "docker-vm-${random_id.instance_id.hex}"
 machine_type = "n1-standard-1"
 zone         = "us-west1-a"

 boot_disk {
   initialize_params {
     image = "ubuntu-os-cloud/ubuntu-1604-lts"
   }
 }

// Make sure we run the docker-compose on startup. ToDo Technicl Debt: convert to a external file this script.
 metadata_startup_script = "sudo apt-get -y update;sudo apt-get -yq install build-essential;sudo apt-get -yq install default-jdk;sudo apt-get -yq install maven;sudo curl -L \"https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)\" -o /usr/bin/docker-compose;sudo chmod +x /usr/bin/docker-compose;sudo apt-get -yq install docker.io;sudo git clone https://github.com/rulyone/apirest.git;cd apirest;sudo git fetch;sudo git merge origin/master;sudo mvn package -Dspring.profiles.active=integrationtest;sudo docker volume create --name=postgres-data;sudo docker build -t apirest .;sudo docker-compose up -d;echo echo"

 network_interface {
   network = "default"

   access_config {
     // Include this section to give the VM an external ip address
     nat_ip = google_compute_address.static.address
   }
 }
}

output "ip" {
  value = google_compute_instance.default.network_interface.0.access_config.0.nat_ip
}

resource "google_compute_firewall" "default" {
  name = "docker-vm-firewall"
  network = "default"

  allow {
    protocol = "tcp"
    ports = ["8080", "5432"]
  }
}
