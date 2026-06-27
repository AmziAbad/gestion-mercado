export interface TableColumn {
  key: string;
  label: string;
  type?: 'text' | 'currency' | 'date' | 'status' | 'boolean';
}

export interface TableAction {
  id: string;
  label: string | ((row: any) => string);
  tone?:
    | 'primary'
    | 'secondary'
    | 'danger'
    | 'success'
    | ((row: any) => 'primary' | 'secondary' | 'danger' | 'success');
}

export interface TableActionEvent {
  action: TableAction;
  row: unknown;
}
