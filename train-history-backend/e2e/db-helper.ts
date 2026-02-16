import mysql, { RowDataPacket } from 'mysql2/promise';

export interface TrainInfo {
  train_number: number;
  departure_date: string;
}

export async function getLatestCompositionInfo(): Promise<TrainInfo> {
  const connection = await mysql.createConnection({
    host: 'localhost',
    user: 'rail_history',
    password: 'rail_pass123',
    database: 'trainhistoryjson',
    dateStrings: true, // Ensure DATE fields are returned as strings
  });
  const [rows] = await connection.execute<RowDataPacket[]>(
    `select train_number, departure_date
     from composition
     where deleted is null
     order by departure_date desc, train_number desc
     limit 1`
  );
  await connection.end();
  return rows[0] as TrainInfo;
}

export async function getLatestTrainInfo(): Promise<TrainInfo> {
  const connection = await mysql.createConnection({
    host: 'localhost',
    port: 3306,
    user: 'rail_history',
    password: 'rail_pass123',
    database: 'trainhistoryjson',
    dateStrings: true, // Ensure DATE fields are returned as strings
  });
  const [rows] = await connection.execute<RowDataPacket[]>(
    `select train_number, departure_date
     from train
     where deleted is null
     order by departure_date desc, train_number desc
     limit 1`
  );
  await connection.end();
  return rows[0] as TrainInfo;
}
