
import boto3
from pathlib import Path
from datetime import datetime, timezone

BUCKET_NAME = "stockpilot-data-storage-unique123"
REGION = "eu-north-1"

def upload_to_s3():
    s3 = boto3.client("s3", region_name=REGION)

    output_dir = Path(__file__).resolve().parents[1] / "output"

    if not output_dir.exists():
        raise FileNotFoundError("Output folder not found")

    csv_files = list(output_dir.glob("*.csv"))

    if not csv_files:
        raise FileNotFoundError("No CSV files found")

    timestamp = datetime.now(timezone.utc).strftime("%Y%m%d_%H%M%S")

    for file in csv_files:
        key = f"etl-exports/{timestamp}/{file.name}"

        s3.upload_file(
            str(file),
            BUCKET_NAME,
            key
        )

        print(f"[S3] Uploaded: {file.name}")

    print(f"Successfully uploaded {len(csv_files)} CSV files!")

if __name__ == "__main__":
    upload_to_s3()
