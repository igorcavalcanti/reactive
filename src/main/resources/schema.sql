-- CreateTable
CREATE TABLE IF NOT EXISTS "Worker" (
    "id" SERIAL NOT NULL,
    "name" TEXT NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT false,
    "profession" TEXT NOT NULL,

    CONSTRAINT "Worker_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE IF NOT EXISTS "Facility" (
    "id" SERIAL NOT NULL,
    "name" TEXT NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT false,

    CONSTRAINT "Facility_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE IF NOT EXISTS "Document" (
    "id" SERIAL NOT NULL,
    "name" TEXT NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT false,

    CONSTRAINT "Document_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE IF NOT EXISTS "FacilityRequirement" (
    "id" SERIAL NOT NULL,
    "facility_id" INTEGER NOT NULL,
    "document_id" INTEGER NOT NULL,

    CONSTRAINT "FacilityRequirement_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE IF NOT EXISTS "DocumentWorker" (
    "id" SERIAL NOT NULL,
    "worker_id" INTEGER NOT NULL,
    "document_id" INTEGER NOT NULL,

    CONSTRAINT "DocumentWorker_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE IF NOT EXISTS "Shift" (
    "id" SERIAL NOT NULL,
    "start" TIMESTAMP WITH TIME ZONE NOT NULL,
    "end" WITH TIME ZONE NOT NULL,
    "profession" TEXT NOT NULL,
    "is_deleted" BOOLEAN NOT NULL DEFAULT false,
    "facility_id" INTEGER NOT NULL,
    "worker_id" INTEGER,

    CONSTRAINT "Shift_pkey" PRIMARY KEY ("id")
);

-- AddForeignKey
ALTER table IF EXISTS "FacilityRequirement" DROP CONSTRAINT IF EXISTS "FacilityRequirement_facility_id_fkey";
ALTER TABLE "FacilityRequirement" ADD CONSTRAINT "FacilityRequirement_facility_id_fkey" FOREIGN KEY ("facility_id") REFERENCES "Facility"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER table IF EXISTS "FacilityRequirement" DROP CONSTRAINT IF EXISTS "FacilityRequirement_document_id_fkey";
ALTER TABLE "FacilityRequirement" ADD CONSTRAINT "FacilityRequirement_document_id_fkey" FOREIGN KEY ("document_id") REFERENCES "Document"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER table IF EXISTS "DocumentWorker" DROP CONSTRAINT IF EXISTS "DocumentWorker_worker_id_fkey";
ALTER TABLE "DocumentWorker" ADD CONSTRAINT "DocumentWorker_worker_id_fkey" FOREIGN KEY ("worker_id") REFERENCES "Worker"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER table IF EXISTS "DocumentWorker" DROP CONSTRAINT IF EXISTS "DocumentWorker_document_id_fkey";
ALTER TABLE "DocumentWorker" ADD CONSTRAINT "DocumentWorker_document_id_fkey" FOREIGN KEY ("document_id") REFERENCES "Document"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER table IF EXISTS "Shift" DROP CONSTRAINT IF EXISTS "Shift_worker_id_fkey";
ALTER TABLE "Shift" ADD CONSTRAINT "Shift_worker_id_fkey" FOREIGN KEY ("worker_id") REFERENCES "Worker"("id") ON DELETE SET NULL ON UPDATE CASCADE;

-- AddForeignKey
ALTER table IF EXISTS "Shift" DROP CONSTRAINT IF EXISTS "Shift_facility_id_fkey";
ALTER TABLE "Shift" ADD CONSTRAINT "Shift_facility_id_fkey" FOREIGN KEY ("facility_id") REFERENCES "Facility"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- Creating index for worker_id
CREATE INDEX IF NOT EXISTS shift_worker_id_idx ON "Shift" (worker_id);
