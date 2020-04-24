// Configure the Google Cloud provider
// CREDENTIALS_FILE.json is outside the project folder for security reasons.
// how to obtain this (getting project credentials): https://cloud.google.com/community/tutorials/getting-started-on-gcp-with-terraform
provider "google" {
  credentials = file("../CREDENTIALS_FILE.json")
  project     = "sage-inn-274818"
  region      = "us-west1"
}

// Terraform plugin for creating random ids
resource "random_id" "instance_id" {
 byte_length = 8
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

// Make sure we run the docker-compose on startup.
 metadata_startup_script = <<EOF
 sudo apt-get -y update;
 sudo apt-get -yq install build-essential; 
 sudo apt-get -yq install default-jdk; 
 sudo apt-get -yq install maven; 
 sudo apt-get -yq install docker-compose docker.io;
 git clone https://github.com/rulyone/apirest.git;
 cd apirest;
 git fetch; git merge origin/master;
 mvn package -Dspring.profiles.active=integrationtest;
 docker build -t apirest .;
 docker-compose up
 EOF

 network_interface {
   network = "default"

   access_config {
     // Include this section to give the VM an external ip address
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